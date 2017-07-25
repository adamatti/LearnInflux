package adamatti

import groovy.util.logging.Slf4j
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Point

@Slf4j
class Main {
	private static final Map cfg = [
		url: "http://localhost:8086",
		user: "root",
		pass: "root",
		db: "sampleDb"
	]

	static void main(String [] args){

		log.info "Started"

		InfluxDB influxDB = connectToInflux(cfg)

		while (true) {
			sendMessage(influxDB)
			Thread.sleep(1000)
		}

		influxDB.close()

		log.info "Done"
	}

	private static InfluxDB connectToInflux(Map cfg){
		InfluxDB influxDB = InfluxDBFactory.connect(cfg.url, cfg.user, cfg.pass)
		influxDB.createDatabase(cfg.db)
		influxDB.setDatabase(cfg.db)
		influxDB
	}

	private static sendMessage(InfluxDB influxDB){
		Random random = new Random()

		int value = random.nextInt(1000)

		Point point = Point.measurement("monitors")
				.tag(type: "SAMPLE_EVENT")
				.fields(value:  value)
				.build()

		influxDB.write(point)

		log.info "msg sent [value: ${value}]"
	}
}
