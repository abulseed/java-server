.PHONY: dev java nginx stop

# Start Java app and NGINX
dev: java nginx

# Start Java App
java:
	./gradlew bootRun &

# Build and start NGINX
nginx:
	docker-compose up --build

# Stop everything
stop:
	docker compose -f docker-compose.yml down
	# Kill Java app (optional, force kill by port)
	@lsof -ti :8080 | xargs kill -9 || true
	@lsof -ti :8081 | xargs kill -9 || true
