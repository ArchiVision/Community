#!/bin/bash

function wait_for_service() {
    local service_name=$1
    local url=$2
    local retry_count=20
    local retry_interval=10

    echo "Waiting for $service_name to be ready at $url..."

    for i in $(seq 1 $retry_count); do
        if curl -s $url > /dev/null; then
            echo "$service_name is up!"
            return 0
        fi
        echo "Waiting for $service_name... (Attempt $i/$retry_count)"
        sleep $retry_interval
    done

    echo "Error: $service_name is not responding at $url"
    return 1
}

BASE_DIR=$(pwd)
REBUILD_CONTAINERS=false

# Check for rebuild flag
if [[ "$1" == "--rebuild" ]]; then
    REBUILD_CONTAINERS=true
fi

# Start ELK stack
echo "Starting ELK stack..."
docker-compose -f "$BASE_DIR/elk-compose/docker-compose.yml" up -d

# Wait for ELK stack to be up
wait_for_service "Elasticsearch" "http://localhost:9200" || exit 1
wait_for_service "Kibana" "http://localhost:5601" || exit 1

# Start app stack
echo "Starting app stack..."
if [ "$REBUILD_CONTAINERS" = true ]; then
    docker-compose -f "$BASE_DIR/docker-compose.yml" up -d --build
else
    docker-compose -f "$BASE_DIR/docker-compose.yml" up -d
fi

# Wait for the app to be up (modify according to your app's health check)
wait_for_service "App" "http://localhost:8080/health" || exit 1

# Start Grafana stack
echo "Starting Grafana stack..."
docker-compose -f "$BASE_DIR/monitoring-compose/docker-compose.yml" up -d

echo "All services are up and running!"
