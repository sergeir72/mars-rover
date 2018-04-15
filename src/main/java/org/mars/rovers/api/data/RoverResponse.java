package org.mars.rovers.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * POJO to handle rover response from NASA API
 * @link https://api.nasa.gov/api.html#MarsPhotos
 *
 * generated with jsonschema2pojo
 * @link http://www.jsonschema2pojo.org/
 *
 */
@Data
public class RoverResponse {
	@SerializedName("photos")
	@Expose
	List<RoverPhoto> photos;
}
