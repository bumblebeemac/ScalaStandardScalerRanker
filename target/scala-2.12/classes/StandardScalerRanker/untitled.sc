val aList = List(1,2,3,4,5)
val aSum = aList.foldLeft(0)(_ - _)

val bList = List("B", "C", "D")
val sumExpr = bList.map(x => "norm_" + x).mkString(" + ")
