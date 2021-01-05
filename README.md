# tiny-weather-bulletin
This project gets next three days forecast with these specific information: average minimum temperature, average maximum temperature, average humidity.
Assumptions:
 - data are calculated by averaging over 3 days and during/outside working hours
 - default working hours: 09:00 - 18:00 considered at specific city local time
 - next day depends on specific location. No difference between working or non-working day
 
Installation:

	mvn clean install
	mvn spring-boot:run -Dspring-boot.run.arguments=--api.openweather.appid=YOUR_APP_ID

Documentation available here: http://localhost:8082/swagger-ui/
