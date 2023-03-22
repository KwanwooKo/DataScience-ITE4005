import sys
import re

min_sup = int(sys.argv[1])
input_file_path = sys.argv[2]
output_file_path = sys.argv[3]

# input 이용해서 trx_list 만드는 함수
def make_trx_list_from_input(input_fd):
    trx_list = []
    # input.txt 에서 읽어오기
    # file을 한 줄 단위로 받아오고 -> \t 를 기준으로 split
    while True:
        line = input_fd.readline()
        if not line:
            break
        single_trx = line.split('\t')
        single_trx[-1] = re.sub('\n', "", single_trx[-1])
        single_trx = list(map(int, single_trx))

        trx_list.append(single_trx)
    return trx_list

# trx_list 출력하는 코드, Debug 용도
def print_trx_list(trx_list):
    for i in range(len(trx_list)):
        print(f"{i}:", end=" ")
        for j in range(len(trx_list[i])):
            print(f"{trx_list[i][j]}", end=" ")
        print()

def print_items(items):
    for i in range(len(items)):
        if i in items:
            print(f"{i}: {items[i]}")

def print_frequents(frequents):
    for frequent in frequents:
        print(frequent)

#################### first set 만들기 ##############################
# item 생성
def make_items(trx_list):
    items = []
    for i in range(len(trx_list)):
        for j in range(len(trx_list[i])):
            items.append(trx_list[i][j])
    return list(set(items))

# 첫번째 candidate 생성
def make_first_candidate(items):
    candidate: set = set()
    for i in range(len(items)):
        candidate.add(items[i])
    return candidate

def make_first_pruned_candidate(items, trx_list):
    pruned_candidate = set()
    for k in range(len(items)):
        count = 0
        for i in range(len(trx_list)):
            for j in range(len(trx_list[i])):
                if items[k] == trx_list[i][j]:
                    count += 1
            if count >= min_sup:
                pruned_candidate.add(items[k])
    return pruned_candidate



def make_first_frequent(items, trx_list):
    frequent = {}
    for k in range(len(items)):
        count = 0
        for i in range(len(trx_list)):
            for j in range(len(trx_list[i])):
                if items[k] == trx_list[i][j]:
                    count += 1
                if count >= min_sup:
                    frequent[items[k]] = count
    return frequent
#################### first set 만들기 ##############################

def self_join(pruned_candidate, k):
    pruned_candidate = list(pruned_candidate)
    ret = set()
    for i in range(len(pruned_candidate)):
        for j in range(len(pruned_candidate)):
            if i >= j: continue
            if type(pruned_candidate[i]) == int:
                ret.add((pruned_candidate[i], pruned_candidate[j]))
            else:
                element = frozenset(pruned_candidate[i]) | frozenset(pruned_candidate[j])
                if len(element) == k + 1:
                    ret.add(element)
    return ret


# helper function, itemSet이 tuple이 아닐 수 있어
def compare_trx_and_itemSet(trx: list, itemSet: set):
    findItemSet = True
    for item in itemSet:
        findSingleItem = False
        for trxItem in trx:
            if item == trxItem:
                findSingleItem = True
                break
        if findSingleItem is False:
            findItemSet = False
            break
    return findItemSet

# frequents.append(ret) 이렇게 해줘야 함
def compare_trxList_and_frequent(trxList: list[list], candidate: set):
    ret = dict()
    for itemSet in candidate:
        count = 0
        for trx in trxList:
            if compare_trx_and_itemSet(trx, itemSet) is True:
                count += 1
        ret[itemSet] = count
    # print(f"ret = {ret}")
    return ret

# helper function, itemSet이 tuple이 아닐 수 있어
def compare_trx_and_single(trx: list, item):
    findItem = False
    for trxItem in trx:
        if item == trxItem:
            findItem = True
            break
    return findItem


def make_pruned_candidate(frequent: dict):
    pruned_candidate = set()
    for i in frequent:
        if frequent[i] >= min_sup:
            pruned_candidate.add(i)
    return pruned_candidate










































