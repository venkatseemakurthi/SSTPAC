package com.macys.sstpac.menuloading.treeStructure;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
	public T data;
	public List<Node<T>> children;

	/**
	 * Default ctor.
	 */
	public Node() {
		super();
	}

	/**
	 * Convenience ctor to create a Node with an instance of T.
	 * 
	 * @param data
	 *            an instance of T.
	 */
	public Node(T data) {
		this();
		setData(data);
	}

	/**
	 * Return the children of Node. The Tree is represented by a single
	 * root Node whose children are represented by a List. Each of
	 * these Node elements in the List can have children. The getChildren()
	 * method will return the children of a Node.
	 * 
	 * @return the children of Node
	 */
	public List<Node<T>> getChildren() {
		if (this.children == null) {
			return new ArrayList<Node<T>>();
		}
		return this.children;
	}

	/**
	 * Sets the children of a Node object. See docs for getChildren() for
	 * more information.
	 * 
	 * @param children
	 *            the List to set.
	 */
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

	/**
	 * Returns the number of immediate children of this Node.
	 * 
	 * @return the number of immediate children.
	 */
	public int getNumberOfChildren() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}

	/**
	 * Adds a child to the list of children for this Node. The addition of
	 * the first child will create a new List
	 * 
	 * @param child
	 *            a Node object to set.
	 */
	public void addChild(Node<T> child) {
		if (children == null) {
			children = new ArrayList<Node<T>>();
		}
		children.add(child);
	}

	/**
	 * Inserts a Node at the specified position in the child list. Will *
	 * throw an ArrayIndexOutOfBoundsException if the index does not exist.
	 * 
	 * @param index
	 *            the position to insert at.
	 * @param child
	 *            the Node object to insert.
	 * @throws IndexOutOfBoundsException
	 *             if thrown.
	 */
	public void insertChildAt(int index, Node<T> child)
			throws IndexOutOfBoundsException {
		if (index == getNumberOfChildren()) {
			// this is really an append
			addChild(child);
			return;
		} else {
			children.get(index); // just to throw the exception, and stop here
			children.add(index, child);
		}
	}

	/**
	 * Remove the Node element at index index of the List
	 * 
	 * @param index
	 *            the index of the element to delete.
	 * @throws IndexOutOfBoundsException
	 *             if thrown.
	 */
	public void removeChildAt(int index) throws IndexOutOfBoundsException {
		children.remove(index);
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(getData().toString()).append(",[");
		int i = 0;
		for (Node<T> e : getChildren()) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(e.getData().toString());
			i++;
		}
		sb.append("]").append("}");
		return sb.toString();
	}
}
