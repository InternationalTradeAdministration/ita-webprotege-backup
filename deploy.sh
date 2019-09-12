mvn azure-functions:deploy
az functionapp config appsettings set \
  --name ita-webprotege-backup \
  --resource-group ITA_DataServices \
  --settings AZURE_STORAGE_ACCOUNT=$TARIFFTOOL_AZURE_STORAGE_ACCOUNT \
  AZURE_STORAGE_ACCOUNT_KEY=$TARIFFTOOL_AZURE_STORAGE_ACCOUNT_KEY \
  AZURE_STORAGE_CONTAINER=$TARIFFTOOL_AZURE_STORAGE_CONTAINER \
  SKOS_WITH_UNESKOS_URL=$SKOS_WITH_UNESKOS_URL \
  SKOS_URL=$SKOS_URL