java  -Xmx7G   -cp ../../target/nova-1.4-SNAPSHOT.jar com.infoblazer.gp.Application ^
--spring.datasource.url=jdbc:mariadb://localhost:3306/adt ^
--spring.datasource.username=userid ^
--spring.datasource.password=password ^
--description="MGHENMG ADT Prediction 1-100" ^
--trials=1  ^
--tournamentSize=4                     ^
--logMetrics=true                      ^
--visualize=true                         ^
--allowTrivialPredictions=false        ^
--maxPrediction=10                     ^
--minPrediction=-10                    ^
--startTrain=31   ^
--endTrain=130   ^
--startTest=31    ^
--endTest=130     ^
--returnType=number                    ^
--applicationName=linearRegressionApp  ^
--elitist=true                         ^
--programType=Prediction               ^
--target=MGHENMG                       ^
--direction=asc                        ^
--populationSize=3000                  ^
--trainingWindow=110                   ^
--maxInitDepth=5                       ^
--maxDepth=10                          ^
--mutationPct=10                       ^
--crossoverPct=90                      ^
--trainingGenerations=41               ^
--selectionStrategy=tournamentSelectionStrategy ^
--functions="add,subtract,multiply,divide,sin,cos,sqrt,exp,ln" ^
--terminals="randomInteger(-1 110),offsetValueFixed(MGHENMG 1),offsetValueFixed(MGHENMG 2),offsetValueFixed(MGHENMG 3),offsetValueFixed(MGHENMG 4),offsetValueFixed(MGHENMG 5),offsetValueFixed(MGHENMG 6),offsetValueFixed(MGHENMG 7),offsetValueFixed(MGHENMG 8),offsetValueFixed(MGHENMG 9),offsetValueFixed(MGHENMG 10),offsetValueFixed(MGHENMG 11),offsetValueFixed(MGHENMG 12),offsetValueFixed(MGHENMG 13),offsetValueFixed(MGHENMG 14),offsetValueFixed(MGHENMG 15),offsetValueFixed(MGHENMG 16),offsetValueFixed(MGHENMG 17),offsetValueFixed(MGHENMG 18),offsetValueFixed(MGHENMG 19),offsetValueFixed(MGHENMG 20),offsetValueFixed(MGHENMG 21),offsetValueFixed(MGHENMG 22),offsetValueFixed(MGHENMG 23),offsetValueFixed(MGHENMG 24),offsetValueFixed(MGHENMG 25),offsetValueFixed(MGHENMG 26),offsetValueFixed(MGHENMG 27),offsetValueFixed(MGHENMG 28),offsetValueFixed(MGHENMG 29),offsetValueFixed(MGHENMG 30),offsetValueFixed(MGHENMG 31)"