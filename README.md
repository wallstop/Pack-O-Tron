Pack-O-Tron
===========

Solution for /r/dailyprogrammer challenge 115 [Difficult] found at:

http://www.reddit.com/r/dailyprogrammer/comments/15uohz/122013_challenge_115_difficult_packotron_5000/

A simplification of the NP-hard packing problem.


Explanation
-----------

**KnapsackObjects** are "genes". Each one of these is a specific item that is to be placed within the knapsack, including orientation (whether or not the item is rotated). There is a public function to swap the width and length (rotate).

**Specimens** are a collection of an ordered list of KnapsackObjects (ArrayList<KnapsackObject>), a knapsack (int [][]), and a fitness (as well as a handy int keeping track of the number of KnapsackObjects, which is equivalent to the size of the ArrayList). 

The ordered list of KnapsackObjects, including orientation, symbolizes the **genome**.

The **fitness function** attempts to fit every object as close to the upper-left corner of the knapsack as possible, starting with the first item on the list. Thus, both the ordering and orientation matter. In the case of this algorithm, a lower fitness is desirable, as a lower fitness represents a smaller bounding box. A fitness for a knapsack configuration is equivalent to the area of it's bounding box. A fitness for a non-fitting configuration (a knapsack configuration where not every item was able to be placed in the knapsack) is equal to the (area of the knapsack) * (number of items unable to be fit) * (1000). Among non-fitting configurations, those that manage to fit the most items are "more fit". This allows for a nice ordering of configurations, where: a configuration with hardly any items  < a configuration fitting almost all of the items << a fitting configuration < a fitting configuration with a small bounding box.

For **mutation**, the orientation and position in the array of a random KnapsackObject is found from one parent, and placed in the other (the equivalent object being deleted). From there, objects may be swapped, orientation may be changed, or the entire list may be reversed depending on a random chance. 

Parents are also randomly determined among the following: The two most fit members, the most fit and a random member, or two random members.
