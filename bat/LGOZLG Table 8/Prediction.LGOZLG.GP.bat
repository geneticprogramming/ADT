java  -Xmx7G   -cp ../../target/nova-1.4-SNAPSHOT.jar com.infoblazer.gp.Application ^
--spring.datasource.url=jdbc:mariadb://localhost:3306/adt ^
--spring.datasource.username=userid ^
--spring.datasource.password=password ^
--description="LGOZLG GP Prediction"  ^
--trials=1  ^
--tournamentSize=4                     ^
--logMetrics=true                      ^
--visualize=false                         ^
--allowTrivialPredictions=false           ^
--maxPrediction=10                           ^
--minPrediction=-10                          ^
--startTrain=150   ^
--endTrain=250    ^
--startTest=150    ^
--endTest=250     ^
--returnType=number                          ^
--applicationName=linearRegressionApp        ^
--elitist=true                               ^
--programType=Prediction                     ^
--target=LGOZLG                              ^
--direction=asc                              ^
--populationSize=3000                        ^
--trainingWindow=110                         ^
--maxInitDepth=5                             ^
--maxDepth=10                                ^
--mutationPct=10                             ^
--crossoverPct=90                            ^
--trainingGenerations=41                     ^
--selectionStrategy=tournamentSelectionStrategy ^
--functions="add,subtract,multiply,divide,sin,cos,sqrt,exp,ln" ^
--terminals="randomInteger(-1 110),offsetValueFixed(LGOZLG 1),offsetValueFixed(LGOZLG 2)"