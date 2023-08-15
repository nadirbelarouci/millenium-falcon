#!/usr/bin/env bash

# Ensure the JSON file is provided as an argument
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <path_to_millenium_falcon_file>"
    exit 1
fi

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is not installed. Please install jq to proceed."
    exit 1
fi

jsonfile=$1
# Convert relative path to absolute path
if [[ ! "$jsonfile" = /* ]]; then
    jsonfile="$PWD/$1"
fi

# Extract routes_db property
routes_db=$(jq -r '.routes_db' "$jsonfile")
dir_of_routes_db=$(dirname "$routes_db")


# Check if routes_db path is relative or absolute
if [[ ! "$routes_db" = /* ]]; then
    # Convert relative path to absolute path
    dir_of_jsonfile=$(dirname "$jsonfile")
    export SQLDB_PATH="$dir_of_jsonfile/$dir_of_routes_db"
else
    export SQLDB_PATH="$dir_of_routes_db"
fi

# Change JSON content
routes_db_file_name=$(basename "$routes_db")
export FALCON_CONFIG=$(jq -c --arg key "routes_db" --arg value "db/$routes_db_file_name" '.[$key]=$value' "$jsonfile")

echo "Falcon config: $FALCON_CONFIG"
echo "Mounting $SQLDB_PATH to /home/starwars/db"

mvn -T1.0C clean package -DskipTests -Dverification.skip -Plocal-dev
docker-compose up -d --build