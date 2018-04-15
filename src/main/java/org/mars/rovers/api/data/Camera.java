package org.mars.rovers.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/**
 * POJO to handle rover response from NASA API
 * @link https://api.nasa.gov/api.html#MarsPhotos
 *
 * generated with jsonschema2pojo
 * @link http://www.jsonschema2pojo.org/
 */
@Data
public class Camera {
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("rover_id")
	@Expose
	private int roverId;
	@SerializedName("full_name")
	@Expose
	private String fullName;
}