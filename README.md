This is simple use case of Rover API (https://api.nasa.gov/api.html#MarsPhotos) to load provided photos to local files.

To get dates of interest we using task file, that contains date in format d-MMM-yy (e.g 1-Mar-18) per line.

Available configuration options:

	key - api key for NASA api. Default setting is DEMO_KEY;
	roverURL - NASA rover api template. This is configurable for future expansions. Default is https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date={DATE}&api_key={KEY} where DATE and KEY are templated variables.
	numberOfImageLoaders - number of threads to do image pulling. This is dependent on your CPU. Default it 8
	dumpJSON - true/false (default). Allows to store response json in files in root of your output directory, so you can see what happened and may be check out some additional information
	timeoutInSeconds - defines how long you can wait till all images loaded. Default is 300s.
	taskFile - full path to task file
	baseOutputDirectory - directory to where core of output should be directed. Subdirectories will be created to indicate which date was used to form requests

Useage:

RoverPullingTaskConfiguration configuration =
        RoverPullingTaskConfiguration.buildDefaultConfiguration(
            TASK_FILE_PATH,
            SOME_DIRECTORY);
						
		RoverPullingTaskApp app = new RoverPullingTaskApp();

		app.execute(configuration);
		
				