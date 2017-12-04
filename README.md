# Discrete Optimization

## Constraint Programming
* Decision variable
* Domain: Set of possible values of a decision variable
* Constraint, e.g. arithmetic constraint between variables like v1 != v2, ...
* Computational model
  * Search: Adds new constraints to the model by choosing a path
  * Constraint store
    * Feasibility: Is there a solution?
    * Pruning: Remove values from the domains that can be excluded because they do not lead to solutions given the current state of constraints in the system
* Reification
* Global constraint: Constraint that takes into account multiple / all decision variables
  * Examples: alldifferent, knapsack
* Redundant constraint
* Symmetry breaking: Eliminate symmetric solutions
* Dual modeling

### Search heuristics
* First-fail principle: Take the "hardest" variable that is most likely to fail first, e.g. queen in the middle of the board (n-queens), country with a lot of neighbours (map selection), ...
  * Variable / value labeling: Take a variable and then assign it a value
    * Variable ordering
      * Choose the variable that is most constrained
        * Variable with the smallest domain
        * Variable that interacts with a maximum of other variables
        * Examples: In map selection take the country with the fewest colors left, if tie: take the next unassigned country that has a maximum of neighbours (=> will eliminate a lot of values)
    * Value ordering
      * Choose the variable that leaves as many options as possible 
    * For optimization problems: Make use of the objective function
  * Value / variable labeling: Take a value and assign it a variable
  * Domain Splitting: Search by not choosing a given value but a given range of values
  * Symmetry Breaking: 
  
### Problems
* n-queens
* Magic series
* Stable mariage
* Binary knapsack
* Euler Knight

