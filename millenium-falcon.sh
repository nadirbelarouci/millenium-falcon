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

mvn -T1.0C clean package -DskipTests -Dverification.skip -Plocal-dev


jsonFile="$1"
jsonFileDir=$(dirname "$jsonFile")
dirOfRoutesDb=$(jq -r '.routes_db' "$jsonFile" | xargs dirname)

# Export the directory path as SQLDB_PATH
export SQLDB_PATH="$PWD/$jsonFileDir/$dirOfRoutesDb"

jsonWithoutRoutesDb=$(jq 'del(.routes_db)' "$jsonFile")

# Set the STARWARS_JAVA_OPTIONS with the content of the JSON file
export STARWARS_JAVA_OPTIONS="-Dspring.application.json=$jsonWithoutRoutesDb"

echo "Mounting $SQLDB_PATH to /home/starwars/db"

docker-compose up -d --build