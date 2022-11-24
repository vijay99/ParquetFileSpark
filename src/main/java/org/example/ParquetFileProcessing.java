
package org.example;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// $example on:schema_merging$

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.Cube;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParquetFileProcessing {


    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL data sources example")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();


        runBasicParquetExample(spark);


        spark.stop();
    }


    private static void runBasicParquetExample(SparkSession spark) {
        // $example on:basic_parquet_example$
       // Dataset<Row> peopleDF = spark.read().json("examples/src/main/resources/people.json");

        // DataFrames can be saved as Parquet files, maintaining the schema information
        //peopleDF.write().parquet("people.parquet");

        // Read in the Parquet file created above.
        // Parquet files are self-describing so the schema is preserved
        // The result of loading a parquet file is also a DataFrame
        Dataset<Row> parquetFileDF = spark.read().parquet("D:\\Parquet/userdata1.parquet");

        // Parquet files can also be used to create a temporary view and then used in SQL statements
        parquetFileDF.createOrReplaceTempView("parquetFile");
        Dataset<Row> namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19");
        Dataset<String> namesDS = namesDF.map(
                (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                Encoders.STRING());
        namesDS.show();
        // +------------+
        // |       value|
        // +------------+
        // |Name: Justin|
        // +------------+
        // $example off:basic_parquet_example$
    }


}