MVN=$(shell which mvn)
MVN_ARGS+=-T 2C -X -e

help: list

clean:
	$(MVN) clean

.PHONY: test
test:
	sh tests.sh

install:
	$(MVN) $(MVN_ARGS) clean compile -U install

install-notests:
	$(MVN) $(MVN_ARGS) clean install -Dmaven.test.skip=true -DskipTests=true

datamock:
	python2 resources/data-provider-mock.py


.PHONY: list
list:
	@$(MAKE) -pRrq -f $(lastword $(MAKEFILE_LIST)) : 2>/dev/null | awk -v RS= -F: '/^# File/,/^# Finished Make data base/ {if ($$1 !~ "^[#.]") {print $$1}}' | sort | egrep -v -e '^[^[:alnum:]]' -e '^$@$$' | xargs

run:
	$(MVN) exec:java -Dexec.mainClass=reactive.Runner

debug:
	mvnDebug exec:java -Dexec.mainClass=reactive.Runner
