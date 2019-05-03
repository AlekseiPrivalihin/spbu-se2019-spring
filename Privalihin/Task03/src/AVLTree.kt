import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class AVLTree<K : Comparable<K>, V> : BalancedSearchTree<K, V>() {
    inner class AVLNode(_key: K, _value: V, _parent: Node?) : Node(_key, _value, _parent) {
        var subtreeSizeDifference = 0

        override fun print(indentation: Int, side: Int) {
            this.left?.print(indentation + 1, -1)

            for (i in 1..indentation) {
                print(" ")
            }

            when(side) {
                -1 -> print("/")
                1 -> print("\\")
            }
            println("$key $value $subtreeSizeDifference")

            this.right?.print(indentation + 1, 1)
        }
    }

    override fun createNode(key: K, value: V, parent: Node?): Node {
        return AVLNode(key, value, parent)
    }

    override fun insert(key: K, value: V): Node {
        val oldSize = size
        val inserted = super.insert(key, value)

        if (size == oldSize) {
            return inserted
        }

        var curNode = inserted as AVLNode
        var son = curNode
        while (curNode.parent != null) {
            val dad = curNode.parent as AVLNode

            if (dad.left == curNode) {
                dad.subtreeSizeDifference++
            }
            else {
                dad.subtreeSizeDifference--
            }

            if (dad.subtreeSizeDifference == 0) {
                break
            }

            if (abs(dad.subtreeSizeDifference) > 1) {
                if (curNode == dad.left) {
                    if (son == curNode.right) {
                        curNode.rotateLeft()
                        curNode.subtreeSizeDifference += min(0, son.subtreeSizeDifference) - 1
                        son.subtreeSizeDifference += max(0, curNode.subtreeSizeDifference) + 1
                    }

                    dad.rotateRight()
                    dad.subtreeSizeDifference += min(0, curNode.subtreeSizeDifference) - 1
                    curNode.subtreeSizeDifference += max(0, dad.subtreeSizeDifference) + 1
                }
                else {
                    if (son == curNode.left) {
                        curNode.rotateRight()
                        curNode.subtreeSizeDifference += max(0, son.subtreeSizeDifference) + 1
                        son.subtreeSizeDifference += min(0, curNode.subtreeSizeDifference) - 1
                    }

                    dad.rotateLeft()
                    dad.subtreeSizeDifference += max(0, curNode.subtreeSizeDifference) + 1
                    curNode.subtreeSizeDifference += min(0, dad.subtreeSizeDifference) - 1
                }

                break
            }

            son = curNode
            curNode = dad
        }

        return inserted
    }

    override fun print() {
        (root as AVLNode).print(0, 0)
    }
}