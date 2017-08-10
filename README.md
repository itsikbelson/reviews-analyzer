# reviews-analyzer
This is an implementation of reviews analyzer that does the following on a reviews CSV file.
Reviews file is taken from https://www.kaggle.com/snap/amazon-fine-food-reviews
* Find most active users
* Find most commented items
* Find most used words
* Tranlsate reviews

In order to build the application, please run:
`./gradlew build`

In order to execute the application, please run:
`java -Xms500M -Xmx500M -jar build/libs/reviews-analyzer.jar <file-name> --translate=true` 

Application configurations are controlled via `resources/application.yml`

Logs configuration are controlled via `resources/log4j2.yml`
