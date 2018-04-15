package org.mars.rovers.api.data;

import java.util.List;
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
public class Rover {
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("landing_date")
	@Expose
	private String landingDate;
	@SerializedName("launch_date")
	@Expose
	private String launchDate;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("max_sol")
	@Expose
	private int maxSol;
	@SerializedName("max_date")
	@Expose
	private String maxDate;
	@SerializedName("total_photos")
	@Expose
	private int totalPhotos;
	@SerializedName("cameras")
	@Expose
	private List<CameraInfo> cameras = null;
}
	