from functions import *


if __name__ == "__main__":
    # 먼저 실행 인자 받아오는 거 부터 해야될 듯 => file_path 받아오기
    # 파일 open
    input_fd = open(input_file_path, 'r')
    output_fd = open(output_file_path, 'w')

    # 필요하다고 생각되는 값
    # 1. candidates => min_sup 과 관계없이 일단 다 저장
    # 2. pruned_candidates => min_sup 이상인 애들만 저장
    # 3. frequents => L_list 라고 생각하면 됨
    # 4. items => 말 그대로 존재하는 item들 다 저장
    # 5. trx_list => trx 불러오는 애들

    trxList: list[list] = make_trx_list_from_input(input_fd)                       # list 안에 list
    items = make_items(trxList)                                                    # 단일 list
    min_sup = min_sup_count / 100 * len(trxList)
    candidate: set = make_first_candidate(items)                                   # set
    pruned_candidate: set = make_first_pruned_candidate(items, trxList)            # set
    frequents: list[dict] = [{}, make_first_frequent(items, trxList)]              # list 안에 dict, dict의 key가 tuple

    ###################### DEBUG ###############################
    # print_items(items)
    # print(trx_list)
    # print(list(candidate))
    # candidate1 = self_join(pruned_candidate)
    # print(list(candidate1))
    # print(f"before pruned_candidate = {pruned_candidate}")
    # print(f"frequents[-1] = {frequents[-1]}")
    # pruned_candidate = self_join(pruned_candidate)
    # print(f"after pruned_candidate = {pruned_candidate}")
    ###################### DEBUG ###############################


    idx = 1
    while pruned_candidate:
        # candidate = generated from pruned_candidate
        candidate = self_join(pruned_candidate, idx)
        frequent = compare_trxList_and_frequent(trxList, candidate)
        frequent = prune_frequent_by_prevfrequent(frequent, list(pruned_candidate))
        pruned_candidate = make_pruned_candidate(frequent)
        # pruned_candidate = prune_frequent_by_candidate(
        # print(f"pruned_candidate = {pruned_candidate}")
        # print(f"candidate = {candidate}")
        # print(f"frequent = {frequent}")
        frequents.append(frequent)
        idx += 1

    print_frequents(frequents)
    print_support(frequents)

    make_association(frequents, output_fd)

    # 파일 close
    input_fd.close()
    output_fd.close()














