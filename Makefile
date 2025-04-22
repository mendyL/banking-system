
run:
	docker-compose up --build

stop:
	docker-compose down

logs:
	docker-compose logs -f

clean:
	docker-compose down -v

restart: stop run
