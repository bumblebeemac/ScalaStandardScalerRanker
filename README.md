# ScalaStandardRanker
StandardScalerRanker - provides simple functionality that standardizes features by removing mean and scaling to unit 
variance. Then scores based on sum of standardized values and ranks.

# Install
Use the following to add to your build.sbt libraryDependencies.

# Functionality
In Data Science and Analytics, there is a need to standardize features by converting a set of columns usually in a 
Spark dataframe by removing the mean and scaling to unit variance. This library provides you an easy way to standardize 
along with features to sum all standardized values and calculating a rank. 

A typical use-case for the above features occur in the real world when a group of products sold by a company needs to be 
ranked. The criteria for ranking (factors to be standardized) will be determined by people with domain knowledge working 
on the project. As products are usually classified using a hierarchical structure, the function requires you to provide 
the level at which values needs to be grouped by to calculate mean and standard deviation.
