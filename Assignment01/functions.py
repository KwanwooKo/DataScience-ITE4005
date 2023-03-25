import sys
import re

min_sup_count = int(sys.argv[2])
input_file_path = sys.argv[3]
output_file_path = sys.argv[4]
min_sup = 0

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
        for element in frequent:
            if type(element) != int:
                print("{", end=" ")
                for i in element:
                    print(i, end=", ")
                print("}", end=", ")
            else:
                print("{", end=" ")
                print(element, end=" ")
                print("}", end=", ")
        print()

def print_support(frequents):
    for frequent in frequents:
        for element in frequent:
            print(element, ": ", frequent[element], end=" / ")
        print()

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
def compare_trxList_and_frequent(trxList: list[list], candidate: set, min_sup):
    ret = dict()
    for itemSet in candidate:
        count = 0
        for trx in trxList:
            if compare_trx_and_itemSet(trx, itemSet) is True:
                count += 1
        if count >= min_sup:
            ret[itemSet] = count
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


def prune_frequent_by_prevfrequent(frequent, prev_pruned_candidate: list):
    ret = frequent.copy()
    for itemset in frequent:
        list_item_set = list(itemset)
        tmp_list = list_item_set.copy()
        for element in list_item_set:
            if len(tmp_list) == 2:
                tmp_list.remove(element)
                cmp_tuple = int(tmp_list[-1])
            elif len(tmp_list) == 3:
                tmp_list.remove(element)
                cmp_tuple = tuple(sorted(tmp_list))
            else:
                tmp_list.remove(element)
                cmp_tuple = frozenset(sorted(tmp_list))
            if cmp_tuple not in prev_pruned_candidate:
                ret.pop(itemset)
                break
            tmp_list = list_item_set.copy()
    return ret



def make_association(frequents, output_fd, number_of_trxList):
    for i in range(len(frequents)):
        for j in range(len(frequents)):
            first_frequent = frequents[i]
            second_frequent = frequents[j]
            print_sup_and_conf(frequents, first_frequent, second_frequent, output_fd, number_of_trxList)

# helper function
def file_write_by_line(output_fd, first_list, second_list, union_sup, confidence):
    buffer = ""
    buffer += "{"
    for i in range(len(first_list)):
        if i != len(first_list) - 1:
            buffer += f"{first_list[i]},"
        else:
            buffer += f"{first_list[i]}"
    buffer += "}\t"
    buffer += "{"
    for i in range(len(second_list)):
        if i != len(second_list) - 1:
            buffer += f"{second_list[i]},"
        else:
            buffer += f"{second_list[i]}"
    buffer += "}\t"
    buffer += f"{union_sup: .2f}\t"
    buffer += f"{confidence}\n"
    output_fd.write(buffer)

# helper function
def print_sup_and_conf(frequents, first_frequent, second_frequent, output_fd, number_of_trxList):
    for first_set in first_frequent:
        for second_set in second_frequent:
            if first_set == second_set: continue
            # int, int
            if type(first_set) == int and type(second_set) == int:
                first_list = [first_set]
                second_list = [second_set]
            # set, int
            elif type(first_set) != int and type(second_set) == int:
                first_list = list(first_set)
                second_list = [second_set]
                if second_set in first_set: continue
            # int, set
            elif type(first_set) == int and type(second_set) != int:
                first_list = [first_set]
                second_list = list(second_set)
                if first_set in second_set: continue
            # set, set
            elif type(first_set) != int and type(second_set) != int:
                first_list = list(first_set)
                second_list = list(second_set)
                has_intersection = bool(set(first_list).intersection(second_list))
                if has_intersection: continue

            union_set = (frozenset(first_list) | frozenset(second_list))
            union_index = len(union_set)  # frequents에 접근할 수 있는 index
            if len(union_set) == 1:
                union_set = list(union_set)[-1]
            elif len(union_set) == 2:
                union_set = sorted(list(union_set))
                union_set = tuple(union_set)
            else:
                union_set = frozenset(union_set)
            # 합쳤을 때 index 초과되면
            if union_index >= len(frequents):
                continue
            frequent = frequents[union_index]
            if union_set in frequent:
                union_sup = round(frequent[union_set] / number_of_trxList * 100, 2)
                union_sup_count = frequent[union_set]
            else:
                continue
            if union_sup < min_sup_count:
                continue
            first_set_sup = first_frequent[first_set]
            confidence = round(union_sup_count / first_set_sup * 100, 2)
            # first_set의 값을 list 형태로 받아와야 함

            file_write_by_line(output_fd, first_list, second_list, union_sup, confidence)



































