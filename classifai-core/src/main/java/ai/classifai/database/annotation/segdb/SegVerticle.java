/*
 * Copyright (c) 2020 CertifAI Sdn. Bhd.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package ai.classifai.database.annotation.segdb;

import ai.classifai.database.DatabaseConfig;

import ai.classifai.database.annotation.AnnotationVerticle;
import ai.classifai.database.annotation.boundingboxdb.BoundingBoxDbQuery;
import ai.classifai.util.ParamConfig;
import ai.classifai.util.image.AnnotationType;
import ai.classifai.util.image.ImageHandler;
import ai.classifai.util.message.ErrorCodes;
import ai.classifai.util.message.ReplyHandler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

/**
 * Segmentation Verticle
 *
 * @author codenamewei
 */
@Slf4j
public class SegVerticle extends AnnotationVerticle
{
    private static JDBCClient projectJDBCClient;

    public void onMessage(Message<JsonObject> message) {

        if (!message.headers().contains(ParamConfig.getActionKeyword()))
        {
            log.error("No action header specified for message with headers {} and body {}",
                    message.headers(), message.body().encodePrettily());

            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No keyword " + ParamConfig.getActionKeyword() + " specified");
            return;
        }
        String action = message.headers().get(ParamConfig.getActionKeyword());

        if(action.equals(SegDbQuery.retrieveData()))
        {
            this.retrieveData(projectJDBCClient, SegDbQuery.retrieveData(), AnnotationType.SEGMENTATION, message);
        }
        else if(action.equals(SegDbQuery.updateData()))
        {
            this.updateData(projectJDBCClient, SegDbQuery.updateData(), AnnotationType.SEGMENTATION, message);
        }
        else if(action.equals(SegDbQuery.retrieveDataPath()))
        {
            this.retrieveDataPath(projectJDBCClient, SegDbQuery.retrieveDataPath(), message);
        }
        else if (action.equals(SegDbQuery.loadValidProjectUUID()))
        {
            this.loadValidProjectUUID(projectJDBCClient, SegDbQuery.retrieveDataPath(), message);
        }
        else
        {
            log.error("SegVerticle query error. Action did not have an assigned function for handling.");
        }
    }


    @Override
    public void stop(Promise<Void> promise)
    {
        log.info("Seg Verticle stopping...");

        File lockFile = new File(DatabaseConfig.getSegLockFile());

        if(lockFile.exists()) lockFile.delete();
    }

    //obtain a JDBC client connection,
    //Performs a SQL query to create the pages table unless it already existed
    @Override
    public void start(Promise<Void> promise)
    {
        projectJDBCClient = JDBCClient.create(vertx, new JsonObject()
                .put("url", "jdbc:hsqldb:file:" + DatabaseConfig.getSegDb())
                .put("driver_class", "org.hsqldb.jdbcDriver")
                .put("max_pool_size", 30));


        projectJDBCClient.getConnection(ar -> {
            if (ar.failed()) {

                log.error("Could not open a database connection for SegVerticle", ar.cause());
                promise.fail(ar.cause());

            } else {
                SQLConnection connection = ar.result();
                connection.execute(SegDbQuery.createProject(), create -> {
                    connection.close();
                    if (create.failed()) {
                        log.error("SegVerticle database preparation error", create.cause());
                        promise.fail(create.cause());

                    } else
                    {
                        //the consumer methods registers an event bus destination handler
                        vertx.eventBus().consumer(SegDbQuery.getQueue(), this::onMessage);
                        promise.complete();
                    }
                });
            }
        });

    }
}