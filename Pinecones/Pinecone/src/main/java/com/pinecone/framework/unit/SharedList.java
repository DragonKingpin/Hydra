package com.pinecone.framework.unit;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.pinecone.framework.system.prototype.PineUnit;

/**
 *  Pinecone Ursus For Java SharedList
 *  SharedList Author: Genius (https://geniusay.com)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  **********************************************************
 *  Thanks for genius`s contribution.
 *  **********************************************************
 */
public class SharedList<T> extends AbstractList<T> implements List<T>, Serializable, PineUnit {

    public interface SharedListBuilder{

        static <T> SharedList<T> slice(int length, List<T> list){
            return slice(0, length, list);
        }

        static <T> SharedList<T> slice(int start, int length, List<T> list){
            checkStartAndLength(start, length, list);
            return freezeList(start, length, list);
        }

        static <T> SharedList<T> merge(List<T> list){
            listNotNull(list);
            return freedomList(list);
        }

        @SafeVarargs
        static <T> SharedList<T> merge(List<T>...lists){
            List<SharedList<T>> sharedLists = Arrays.stream(lists).map(SharedListBuilder::freezeList).collect(Collectors.toList());
            return new SharedList<>(0,new ArrayList<>(), sharedLists);
        }

        static <T> SharedList<T> freedomList(List<T> list){
            return freedomList(0, freezeList(list));
        }
        static <T> SharedList<T> freedomList(int start, List<T> list){
            return new SharedList<>(0, freezeList(start, list));
        }

        private static <T> SharedList<T> freezeList(List<T> list){
            return freezeList(0, list);
        }

        private static <T> SharedList<T> freezeList(int start, List<T> list){
            return freezeList(start, list.size() - start, list);
        }

        private static <T> SharedList<T> freezeList(int start, int length, List<T> list){
            checkStartAndLength(start, length, list);
            return new SharedList<>(start, length, list);
        }
    }

    private static final int MAX_END_OFFSET = Integer.MAX_VALUE;

    private final int endPtr;

    private final int startPtr;

    private int size;
    private final List<T> elementData;

    private final List<Integer> sharedSizeList;

    private final List<SharedList<T>> sharedLists;


    private SharedList(int startPtr, SharedList<T> sharedList) {
        this(startPtr, MAX_END_OFFSET, new ArrayList<>(), new ArrayList<>(List.of(sharedList)));
    }
    private SharedList(int startPtr, List<T> elementData) {
        this(startPtr, MAX_END_OFFSET, elementData, new ArrayList<>());
    }

    private SharedList(int startPtr, List<T> elementData, List<SharedList<T>> sharedLists) {
        this(startPtr, MAX_END_OFFSET, elementData, sharedLists);
    }

    private SharedList(int startPtr, int endPtr, List<T> elementData) {
        this(startPtr, endPtr, elementData, new ArrayList<>());
    }

    private SharedList(int startPtr, int endPtr, List<T> elementData, List<SharedList<T>> sharedLists) {
        this.endPtr = endPtr;
        this.startPtr = startPtr;
        this.elementData = elementData;
        this.sharedSizeList = new ArrayList<>();
        this.sharedLists = new ArrayList<>();
        this.size = 0;

        initSharedList(sharedLists);
        incrementSizeAndCheckExceed(elementData.size());
    }

    // 初始化 共享 list的时候，如果某个共享list超出当前的endPtr，则停止统计后续size以及sharedSizeList
    private void initSharedList(List<SharedList<T>> sharedLists){
        for (SharedList<T> sharedList : sharedLists) {
            if(sharedList.isEmpty()){
                continue;
            }

            this.sharedLists.add(sharedList);
            int sharedSize = sharedList.size();
            addSharedSizeList(sharedSize);
            if(incrementSizeAndCheckExceed(sharedSize)){
                break;
            }
        }
    }

    private void addSharedSizeList(int sharedSize){
        int size = Math.min(endPtr, sharedSizeList.isEmpty()?sharedSize:sharedSize+sharedSizeList.get(sharedSizeList.size()-1));
        sharedSizeList.add(size);
    }

    private boolean incrementSizeAndCheckExceed(int num){
        int nextSize;
        if((nextSize = size + num) > endPtr){
            size = endPtr;
            return true;
        }
        size = nextSize;
        return false;
    }

    private int selfSizeIndex(){
        return sharedSizeList.size();
    }

    /**
     * 基于startPtr的相对index
     * @param index
     * @return
     */
    private int offsetIndex(int index){
        return startPtr + index;
    }

    /**
     * 映射到对应共享列表的下标
     * @param index
     * @return
     */
    private int invokeIndex(int index, int sharedIndex){
        return offsetIndex(index) - (sharedIndex == 0?0:sharedSizeList.get(sharedIndex-1));
    }

    /**
     * 当为第一个元素时，查看index是否在 [0, sharedSizeList.get(index)] 范围内
     * 当为最后一个元素时，查看index是否在 [sharedSizeList.get(index-1), selfSize()] 范围内
     * @param index
     * @param sharedIndex
     * @return
     */
    private boolean inSizeRange(int index, int sharedIndex){
        int l = sharedIndex == 0?0:sharedSizeList.get(sharedIndex-1);
        int r = sharedIndex == sharedSizeList.size()?selfSize():sharedSizeList.get(sharedIndex);
        return index >= l && index < r;
    }

    // 弱缓存
    private int lastAccess = -1;
    private int findListIndex(int index){

        indexOutOfSizeThrow(index);

        int selfIndex = selfSizeIndex();
        if(sharedLists.isEmpty() && inSizeRange(index, selfIndex)){
            return selfIndex;
        }

        if(lastAccess != -1 && inSizeRange(index, lastAccess)){
            return lastAccess;
        }
        lastAccess = -1;

        int l = 0;
        int r = sharedSizeList.size()-1;
        int mid = (l + r) >> 1;
        while(l < r){
            if(inSizeRange(index, mid)){
                lastAccess = mid;
                return mid;
            }else if(index >= sharedSizeList.get(mid)){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }
        lastAccess = l;
        return l;
    }

    private List<T> findList(int listIndex){

        SharedList<T> sharedList = listIndex == selfSizeIndex()?this:sharedLists.get(listIndex);
        if(sharedList == this){
            return this.elementData;
        }
        return sharedList;
    }

    @Override
    public T get(int index) {
        int listIndex = findListIndex(index);
        int ptr = invokeIndex(index, listIndex);
        return findList(listIndex).get(ptr);
    }

    @Override
    public T set(int index, T element) {
        int listIndex = findListIndex(index);
        int ptr = invokeIndex(index, listIndex);
        return findList(listIndex).set(ptr, element);
    }

    @Override
    public boolean add(T t) {
        incrementSizeThrowOutOfSize(1);
        return this.elementData.add(t);
    }

    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }

    public SharedList<T> subList(){
        return SharedListBuilder.slice(0, this.size(), this);
    }

    public SharedList<T> subList(int start, int length){
        return SharedListBuilder.slice(start, length, this);
    }

    @Override
    public int size() {
        return size;
    }

    public int selfSize(){
        return this.elementData.size();
    }

    private boolean indexOutOfSize(int index){
        return index < 0 || index >= size();
    }

    private void indexOutOfSizeThrow(int index){
        if(indexOutOfSize(index)){
            throw new IndexOutOfBoundsException(String.format("index %s out of range [0, %s]", index, size()));
        }
    }
    private void incrementSizeThrowOutOfSize(int num){
        if (incrementSizeAndCheckExceed(num)) {
            throw new IndexOutOfBoundsException(String.format("size %s + %s out of end ptr %s", size(), num, endPtr));
        }
    }

    private static <T> void checkStartAndLength(int start, int length, List<T> list){

        listNotNull(list);

        if(start < 0 || length < 0){
            throw new IndexOutOfBoundsException("start and length must >= 0");
        }

        int totalLength;
        if((totalLength = start + length) > list.size()){
            throw new IndexOutOfBoundsException(String.format("start + length %s must in range [0, %s)", totalLength, list.size()));
        }
    }

    private static <T> void listNotNull(List<T> list){
        if(Objects.isNull(list)){
            throw new NullPointerException("list is nullptr");
        }
    }
    public Iterator<T> iterator(){
        return new Itr();
    }

    private Iterator<T> skipIterator(int skipNum){
        return new Itr(skipNum);
    }

    private class Itr implements Iterator<T> {

        private int cursor;

        private int nowSharedListIndex = -1;

        private Iterator<T> currentSharedItr = null;

        private Iterator<T> selfItr = null;

        private boolean selfFlag = false;

        private final boolean selfElementIsShared = elementData instanceof SharedList;

        private int lastCursor = -1;

        public Itr() {
        }

        public Itr(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return !indexOutOfSize(cursor);
        }

        /**
         * 采用优化遍历法，不再采用二分的方式去一个个寻找对应的index
         * 而是采用遍历的方式，遍历过程中，如果发现当前的index已经大于等于endPtr，则需要切换到下一个sharedList
         * @return T
         */
        @Override
        public T next() {
            int i = cursor++;

            if(currentSharedItr!=null && currentSharedItr.hasNext()){
                return currentSharedItr.next();
            }

            for(;;){
                if(selfFlag || sharedLists.isEmpty()){
                    int ptr = invokeIndex(i, selfSizeIndex());
                    if(selfElementIsShared){
                        if(selfItr == null) {
                            selfItr = ((SharedList<T>) elementData).skipIterator(ptr);
                        }
                        return selfItr.next();
                    }else{
                        return elementData.get(lastCursor = ptr);
                    }
                }

                nowSharedListIndex++;
                if(nowSharedListIndex>=sharedLists.size()){
                    selfFlag = true;
                    continue;
                }
                currentSharedItr = sharedLists.get(nowSharedListIndex).skipIterator(invokeIndex(i, nowSharedListIndex));

                return currentSharedItr.next();
            }

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (T t : this) {
            sb.append(t).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean containsKey( Object elm ) {
        try {
            if( elm instanceof Number ) {
                int nElm = ( (Number)elm ).intValue();
                int nLength = this.size();
                if( nElm < 0 || nLength == 0 ){
                    return false;
                }
                return nLength > nElm;
            }
            return this.containsKey( Integer.parseInt(elm.toString()) );
        }
        catch ( NumberFormatException e ){
            return false;
        }
    }

    @Override
    public boolean hasOwnProperty( Object elm ) {
        return this.containsKey( elm );
    }
}
