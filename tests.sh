lsof -ti:9500 | xargs kill
lsof -ti:8100 | xargs kill
resources/data-provider-mock.py &
nohup mvn exec:java -Dexec.mainClass=Runner &
sleep 3
mvn robotframework:run
lsof -ti:9500 | xargs kill
lsof -ti:8100 | xargs kill
