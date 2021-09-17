package ai.classifai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Thumbnail {

    @JsonProperty("uuid")
    String uuidParam;

    @JsonProperty("project_name")
    String projectNameParam;

    @JsonProperty("img_path")
    String imgPathParam;

    @JsonProperty("bnd_box")
    JsonArray boundingBoxParam;

    @JsonProperty("img_depth")
    Integer imgDepth;

    @JsonProperty("img_x")
    Integer imgXParam;

    @JsonProperty("img_y")
    Integer imgYParam;

    @JsonProperty("img_w")
    Integer imgWParam;

    @JsonProperty("img_h")
    Integer imgHParam;

    @JsonProperty("file_size")
    Integer fileSizeParam;

    @JsonProperty("img_ori_w")
    Integer imgOriWParam;

    @JsonProperty("img_ori_h")
    Integer imgOriHParam;

    @JsonProperty("img_thumbnail")
    String imgThumbnailParam;
}
