@echo off
setlocal enabledelayedexpansion

set BASE_DIR=%cd%
set REBUILD_CONTAINERS=false

rem Check for rebuild flag
if "%1"=="--rebuild" (
    set REBUILD_CONTAINERS=true
)

rem Function to wait for service
:wait_for_service
set service_name=%1
set url=%2
set retry_count=20
set retry_interval=10

echo Waiting for %service_name% to be ready at %url%...

for /L %%i in (1,1,%retry_count%) do (
    powershell -Command "try { $response = Invoke-WebRequest -Uri %url% -UseBasicParsing; if ($response.StatusCode -eq 200) { exit 0 } } catch { exit 1 }"
    if !errorlevel! == 0 (
        echo %service_name% is up!
        exit /b 0
    )
    echo Waiting for %service_name%... (Attempt %%i/%retry_count%)
    timeout /t %retry_interval% >nul
)

echo Error: %service_name% is not responding at %url%
exit /b 1

rem Start ELK stack
echo Starting ELK stack...
docker-compose -f "%BASE_DIR%\elk-compose\docker-compose.yml" up -d

rem Wait for ELK stack to be up
call :wait_for_service "Elasticsearch" "http://localhost:9200"
if %errorlevel% neq 0 exit /b 1
call :wait_for_service "Kibana" "http://localhost:5601"
if %errorlevel% neq 0 exit /b 1

rem Start app stack
echo Starting app stack...
if "%REBUILD_CONTAINERS%"=="true" (
    docker-compose -f "%BASE_DIR%\docker-compose.yml" up -d --build
) else (
    docker-compose -f "%BASE_DIR%\docker-compose.yml" up -d
)

rem Wait for the app to be up (modify according to your app's health check)
call :wait_for_service "App" "http://localhost:8080/health"
if %errorlevel% neq 0 exit /b 1

rem Start Grafana stack
echo Starting Grafana stack...
docker-compose -f "%BASE_DIR%\monitoring-compose\docker-compose.yml" up -d

echo All services are up and running!
