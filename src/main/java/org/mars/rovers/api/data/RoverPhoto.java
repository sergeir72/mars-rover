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
 * */
@Data
public class RoverPhoto {

	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("sol")
	@Expose
	private int sol;
	@SerializedName("camera")
	@Expose
	private Camera camera;
	@SerializedName("img_src")
	@Expose
	private String imgSrc;
	@SerializedName("earth_date")
	@Expose
	private String earthDate;
	@SerializedName("rover")
	@Expose
	private Rover rover;
}