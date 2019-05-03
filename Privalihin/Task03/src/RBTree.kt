class RBTree<K : Comparable<K>, V> : BalancedSearchTree<K, V>() {
    inner class RBNode(_key: K, _value: V, _parent: Node?) : Node(_key, _value, _parent) {

        var color = Color.RED

        override fun print(indentation: Int, side: Int) {
            this.left?.print(indentation + 1, -1)

            for (i in 1..indentation) {
                print(" ")
            }

            when(side) {
                -1 -> print("/")
                1 -> print("\\")
            }
            println("$key $value $color")

            this.right?.print(indentation + 1, 1)
        }
    }

    override fun createNode(key: K, value: V, parent: Node?): Node {
        return RBNode(key, value, parent)
    }

    override fun insert(key: K, value: V): Node {
        var node = super.insert(key, value) as RBNode
        val inserted = node
        while (node.parent != null) {
            val dad = node.parent as RBNode
            val u = node.uncle() as RBNode?

            when {

                (dad.color == Color.BLACK) -> {
                    return inserted
                }

                (u != null && u.color == Color.RED) -> {
                    dad.color = Color.BLACK
                    u.color = Color.BLACK
                    val grandDad = dad.parent as RBNode // grandparent exists because uncle exists
                    grandDad.color = Color.RED
                    node = grandDad
                }

                else -> {
                    val grandDad = dad.parent as RBNode // grandparent exists because parent is red
                    // and therefore is not root

                    if (node ==  dad.right && dad == grandDad.left) {
                        dad.rotateLeft()
                        node = node.left as RBNode // safe because we just rotated the tree
                    }
                    else if (node ==  dad.left && dad == grandDad.right) {
                        dad.rotateRight()
                        node = node.right as RBNode // safe because we just rotated the tree
                    }

                    val newDad = node.parent as RBNode
                    val newGrandDad = node.grandparent() as RBNode
                    if (node == newDad.left) {
                        newGrandDad.rotateRight()
                    }
                    else {
                        newGrandDad.rotateLeft()
                    }
                    newDad.color = Color.BLACK
                    newGrandDad.color = Color.RED
                    return inserted
                }
            }
        }

        node.color = Color.BLACK
        return inserted
    }

    override fun print() {
        (root as RBNode).print(0, 0)
    }
}