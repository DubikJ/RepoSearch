package com.gmail.vanyadubik.reposearch.utils;

import com.gmail.vanyadubik.reposearch.model.db.Repository;
import com.gmail.vanyadubik.reposearch.model.json.ResultItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Json2DbModelConverter {

    public static Repository convertRepository(ResultItemDTO resultItemDTO, String searchFilter) {
        return new Repository(
                    resultItemDTO.getId(),
                    resultItemDTO.getName(),
                    resultItemDTO.getFull_name(),
                    resultItemDTO.isPriv(),
                    resultItemDTO.getUrl(),
                    resultItemDTO.getDescription(),
                    resultItemDTO.getDateCreate(),
                    resultItemDTO.getDateUpdate(),
                    resultItemDTO.getSize(),
                    resultItemDTO.getWatchers(),
                    resultItemDTO.getScore(),
                    resultItemDTO.getDefBranch(),
                    resultItemDTO.getOwner() != null? resultItemDTO.getOwner().getLogin() : null,
                    resultItemDTO.getOwner() != null? resultItemDTO.getOwner().getAvatar() :null,
                    searchFilter);
    }

    public static List<Repository> convertRepositoryList(List<ResultItemDTO> resultItemDTOList, String searchFilter) {

        if (resultItemDTOList == null) return Collections.emptyList();
        List<Repository> result = new ArrayList<>();
        for (ResultItemDTO resultItemDTO : resultItemDTOList) {
            result.add(new Repository(
                    resultItemDTO.getId(),
                    resultItemDTO.getName(),
                    resultItemDTO.getFull_name(),
                    resultItemDTO.isPriv(),
                    resultItemDTO.getUrl(),
                    resultItemDTO.getDescription(),
                    resultItemDTO.getDateCreate(),
                    resultItemDTO.getDateUpdate(),
                    resultItemDTO.getSize(),
                    resultItemDTO.getWatchers(),
                    resultItemDTO.getScore(),
                    resultItemDTO.getDefBranch(),
                    resultItemDTO.getOwner() != null ? resultItemDTO.getOwner().getLogin() : null,
                    resultItemDTO.getOwner() != null ? resultItemDTO.getOwner().getAvatar() : null,
                    searchFilter));
        }
        return result;
    }
}
