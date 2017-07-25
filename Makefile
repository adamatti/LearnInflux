stop:
	docker-compose stop
	docker-compose rm -fv

deps:
	docker-compose up --build -d
