#!/bin/bash

mvn clean package assembly:assembly -DskipTests -Dmaven.test.skip=true

tar zcvf target/vesta-id-generator-0.0.1-release/bin/vesta-lib-0.0.1.tar.gz -C target/vesta-id-generator-0.0.1-release/lib . 
tar zcvf target/vesta-id-generator-0.0.1-release/bin/vesta-src-0.0.1.tar.gz -C target/vesta-id-generator-0.0.1-release/src .
tar zcvf target/vesta-id-generator-0.0.1-release/bin/vesta-all-src-0.0.1.tar.gz ./vesta* pom.xml make-release.sh .gitignore deploy-maven.sh assembly.xml todo.txt

mkdir ./target/vesta-id-generator-0.0.1-release/doc
#generate-md --layout mixu-book --input ./vesta-doc --output ./target/vesta-id-generator-0.0.1-release/doc
generate-md --layout ./vesta-theme --input ./vesta-doc --output ./target/vesta-id-generator-0.0.1-release/doc

rm -fr releases/
mkdir releases
tar zcvf releases/vesta-id-generator-0.0.1-release.tar.gz -C target vesta-id-generator-0.0.1-release
