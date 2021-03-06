# ScalaStandardScalerRanker
StandardScalerRanker - provides simple functionality that standardizes features by removing mean and scaling to unit 
variance. Then ranks based on sum of all standardized values.

# Install
Use the following to add to your build.sbt libraryDependencies.

# Functionality
In Data Science and Analytics, there is a need to standardize features by converting a set of columns usually in a 
Spark dataframe by removing the mean and scaling to unit variance. This library provides you an easy way to standardize 
features and provides a rank of all items within a group by adding all standardized values.

A typical use-case for the above features occur in the real world when a group of products sold by a company needs to be 
ranked. The criteria for ranking (factors to be standardized) will be determined by users with domain knowledge working 
on the project. As products are usually classified using a hierarchical structure, the function requires you to provide 
the level at which values needs to be grouped by to calculate mean and standard deviation.

Refer to [Example.scala](https://github.com/ravishankars8668/ScalaStandardScalerRanker/blob/main/src/main/scala/Examples/Example.scala) in the Examples package for usage.

Credits: [Kaggle](https://www.kaggle.com/)

Dataset link: [https://www.kaggle.com/ammaraahmad/used-cars-dataset](https://www.kaggle.com/ammaraahmad/used-cars-dataset)

In the example, mean and standard deviation is calculated for standardization factors ("compression_ratio", "horsepower", 
"peak_rpm", "city_mpg", "highway_mpg", "price") after they are grouped by ("make", "aspiration", "num_of_doors", 
"body_style").
