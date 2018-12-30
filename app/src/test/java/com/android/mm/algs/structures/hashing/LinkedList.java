package com.android.mm.algs.structures.hashing;

/**
 * 新插入的值在最前面。
 */
public class LinkedList {
    private Node Head;
    private int size;

    public LinkedList() {
        Head = null;
        size = 0;
    }

    public void insert(int data) {
        Node temp = Head;
        Node newNode = new Node(data);

        size++;
        if (Head == null) {
            Head = newNode;
        } else {
            newNode.next = Head;
            Head = newNode;
        }
    }

    public void delete(int data) {
        if (size == 0) {
            return ;
        } else {
            Node curr = Head;
            if (curr.data == data) {
                Head = curr.next;
                size--;
                return ;
            } else {
                while (curr.next.next != null) {
                    if (curr.next.data == data) {
                        // 移除下一个
                        curr.next = curr.next.next;
                        return ;
                    }
                }
            }
        }
    }

    public void display() {
        Node temp = Head;
        while(temp != null) {
            System.out.printf("%d ",temp.data);
            temp = temp.next;
        }
        System.out.println();
    }
}
