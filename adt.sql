CREATE OR REPLACE TABLE
    gp_run
    (
        id INT NOT NULL AUTO_INCREMENT ,
        xyseries_title VARCHAR(50)NOT NULL,
        run_date DATETIME NOT NULL,
        program_type VARCHAR(50) NOT NULL,
        application_name VARCHAR(255) NOT NULL,
        end_date DATETIME,
        description VARCHAR(255) ,
        run_identifier VARCHAR(50) ,
        CONSTRAINT PK_xyseries_run PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    prediction
    (
        id INT NOT NULL AUTO_INCREMENT ,
        gp_run_id INT NOT NULL,
        prediction_start DATETIME,
        prediction_end DATETIME,
        CONSTRAINT PK_xyseries_prediction PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.prediction_data
    (
        id INT NOT NULL AUTO_INCREMENT ,
        prediction_id INT NOT NULL,
        x FLOAT(53) NOT NULL,
        y_actual FLOAT(53),
        y_predicted FLOAT(53),
        regime INT,
        fittest_program TEXT ,
        fittest_regime_program TEXT ,
        created_on DATETIME,
        fittest_nodecount INT,
        fittest_depth INT,
        fittest_adf_nodecount INT,
        invalid_prediction_count INT,
        prediction_generations INT,
        CONSTRAINT PK_xyseries_prediction_data PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.prediction_regime_branch
    (
        id INT NOT NULL AUTO_INCREMENT ,
        prediction_id INT NOT NULL,
        best_fitness FLOAT(53),
        median_fitness FLOAT(53),
        mean_fitness FLOAT(53),
        variance_fitness FLOAT(53),
        stddev_fitness FLOAT(53),
        mean_nodecount INT,
        median_nodecount INT,
        variance_nodecount INT,
        stddev_nodecount INT,
        mean_depth INT,
        median_depth INT,
        variance_depth INT,
        stddev_depth INT,
        median_adf_nodecount INT,
        mean_adf_nodecount INT,
        variance_adf_nodecount INT,
        stddev_adf_nodecount INT,
        CONSTRAINT PK_xyseries_prediction_regime_branch PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.prediction_result_branch
    (
        id INT NOT NULL AUTO_INCREMENT ,
        prediction_id INT NOT NULL,
        best_fitness FLOAT(53),
        median_fitness FLOAT(53),
        mean_fitness FLOAT(53),
        variance_fitness FLOAT(53),
        stddev_fitness FLOAT(53),
        mean_nodecount INT,
        median_nodecount INT,
        variance_nodecount INT,
        stddev_nodecount INT,
        mean_depth INT,
        median_depth INT,
        variance_depth INT,
        stddev_depth INT,
        median_adf_nodecount INT,
        mean_adf_nodecount INT,
        variance_adf_nodecount INT,
        stddev_adf_nodecount INT,
        CONSTRAINT PK_xyseries_prediction_result_branch PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.training
    (
        id INT NOT NULL AUTO_INCREMENT,
        gp_run_id INT NOT NULL,
        training_start DATETIME NOT NULL,
        training_end DATETIME NOT NULL,
        iteration INT NOT NULL,
        generation INT NOT NULL,
        fitness_evaluations INT,
        fitness_calculations INT,
        in_prediction BIT,
        node_evaluations FLOAT(53),
        CONSTRAINT PK_xyseries_training PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.training_data
    (
        id INT NOT NULL AUTO_INCREMENT,
        training_id INT NOT NULL,
        x FLOAT(53) NOT NULL,
        y_predicted FLOAT(53),
        regime INT,
        y_actual FLOAT(53),
        CONSTRAINT PK_xyseries_training_data PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.training_regime_branch
    (
        id INT NOT NULL AUTO_INCREMENT,
        training_id INT NOT NULL,
        best_fitness FLOAT(53),
        median_fitness FLOAT(53),
        mean_fitness FLOAT(53),
        variance_fitness FLOAT(53),
        stddev_fitness FLOAT(53),
        population_size INT,
        invalid_population_size INT,
        library_population_size INT,
        median_depth INT,
        mean_depth INT,
        variance_depth FLOAT(53),
        stddev_depth FLOAT(53),
        fittest_program TEXT ,
        median_nodecount FLOAT(53),
        mean_nodecount FLOAT(53),
        variance_nodecount FLOAT(53),
        stddev_nodecount FLOAT(53),
        fittest_nodecount INT,
        fittest_depth INT,
        median_adf_nodecount INT,
        mean_adf_nodecount INT,
        variance_adf_nodecount INT,
        stddev_adf_nodecount INT,
        fittest_adf_nodecount INT,
        total_adf_nodecount INT,
        total_nodecount INT,
        CONSTRAINT PK_xyseries_training_regime_branch PRIMARY KEY (id)
    );
CREATE OR REPLACE TABLE
    adt.training_result_branch
    (
        id INT NOT NULL AUTO_INCREMENT,
        training_id INT NOT NULL,
        best_fitness FLOAT(53),
        median_fitness FLOAT(53),
        mean_fitness FLOAT(53),
        variance_fitness FLOAT(53),
        stddev_fitness FLOAT(53),
        population_size INT,
        invalid_population_size INT,
        library_population_size INT,
        median_depth INT,
        mean_depth INT,
        variance_depth FLOAT(53),
        stddev_depth FLOAT(53),
        fittest_program TEXT,
        median_nodecount FLOAT(53),
        mean_nodecount FLOAT(53),
        variance_nodecount FLOAT(53),
        stddev_nodecount FLOAT(53),
        fittest_nodecount INT,
        fittest_depth INT,
        median_adf_nodecount INT,
        mean_adf_nodecount INT,
        variance_adf_nodecount INT,
        stddev_adf_nodecount INT,
        fittest_adf_nodecount INT,
        total_adf_nodecount INT,
        total_nodecount INT,
        CONSTRAINT PK_xyseries_training_result_branch PRIMARY KEY (id)
    );
