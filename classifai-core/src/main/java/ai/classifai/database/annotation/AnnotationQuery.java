/*
 * Copyright (c) 2020-2021 CertifAI Sdn. Bhd.
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
package ai.classifai.database.annotation;

import lombok.Getter;

/**
 * Common SQL Query for Each Annotation Type
 *
 * @author codenamewei
 */
public class AnnotationQuery
{

    @Getter private static final String createProject = "CREATE TABLE IF NOT EXISTS Project (uuid UUID, project_id UUID, img_path VARCHAR(2000), version_list CLOB, img_depth INT, " +
            "img_ori_w INT, img_ori_h INT, file_size INT, PRIMARY KEY(uuid, project_id))";

    @Getter private static final String queryData = "Get data from cache";

    @Getter private static final String updateData = "UPDATE Project SET version_list = ?, img_depth = ?, img_ori_w = ?, img_ori_h = ?, file_size = ? WHERE uuid = ? AND project_id = ?";

    @Getter private static final String createData = "INSERT INTO Project VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Getter private static final String retrieveDataPath = "SELECT img_path FROM Project WHERE uuid = ? AND project_id = ?";

    @Getter private static final String loadValidProjectUuid = "SELECT img_path FROM Project WHERE project_id = ? AND uuid = ?";

    @Getter private static final String deleteProject = "DELETE FROM Project WHERE project_id = ?";

    @Getter private static final String deleteProjectData = "DELETE FROM Project WHERE project_id = ? AND uuid IN (?)";

    @Getter private static final String retrieveAllProjects = "SELECT * FROM Project";

    //v2
    @Getter private static final String queryUuid = "SELECT uuid FROM Project WHERE img_path = ? AND project_id = ?";

    @Getter private static final String extractProject = "SELECT uuid, img_path, version_list, img_depth, img_ori_w, img_ori_h, file_size FROM Project WHERE project_id = ?";

    @Getter private static final String renameProjectData = "UPDATE Project SET img_path = ? WHERE uuid = ? AND project_id = ?";

    @Getter private static final String createVideoProject = "CREATE TABLE IF NOT EXISTS VideoProject (uuid UUID, project_id UUID, video_frame_index INT, video_time_stamp INT, img_path VARCHAR(2000), video_file_path VARCHAR(2000), " +
            " version_list CLOB, img_depth INT, img_ori_w INT, img_ori_h INT, file_size INT, PRIMARY KEY(uuid, project_id))";

    @Getter private static final String createVideoData = "INSERT INTO VideoProject VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Getter private static final String retrieveVideoDataPath = "SELECT img_path FROM VideoProject WHERE uuid = ? AND project_id = ?";

    @Getter private static final String loadValidVideoProjectUuid = "SELECT img_path FROM VideoProject WHERE project_id = ? AND uuid = ?";

    @Getter private static final String deleteVideoProject = "DELETE FROM VideoProject WHERE project_id = ?";

    @Getter private static final String extractVideoProject = "SELECT uuid, img_path, video_frame_index, video_time_stamp, video_file_path, version_list, img_depth, img_ori_w, img_ori_h, file_size FROM VideoProject WHERE project_id = ?";

    @Getter private static final String updateVideoData = "UPDATE VideoProject SET version_list = ?, img_depth = ?, img_ori_w = ?, img_ori_h = ?, file_size = ? WHERE uuid = ? AND project_id = ?";
}
