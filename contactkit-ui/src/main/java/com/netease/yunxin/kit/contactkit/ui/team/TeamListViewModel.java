/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

package com.netease.yunxin.kit.contactkit.ui.team;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.yunxin.kit.common.ui.viewmodel.FetchResult;
import com.netease.yunxin.kit.common.ui.viewmodel.LoadStatus;
import com.netease.yunxin.kit.contactkit.repo.ContactRepo;
import com.netease.yunxin.kit.contactkit.ui.model.ContactTeamBean;
import com.netease.yunxin.kit.corekit.im.provider.FetchCallback;
import com.netease.yunxin.kit.common.ui.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class TeamListViewModel extends BaseViewModel {
    private final MutableLiveData<FetchResult<List<ContactTeamBean>>> resultLiveData = new MutableLiveData<>();
    private final FetchResult<List<ContactTeamBean>> fetchResult = new FetchResult<>(LoadStatus.Finish);
    private final List<ContactTeamBean> teamBeanList = new ArrayList<>();
    private final ContactRepo contactRepo = new ContactRepo();
    private final Observer<List<Team>> teamUpdateObserver;
    private final Observer<Team> teamRemoveObserver;

    public MutableLiveData<FetchResult<List<ContactTeamBean>>> getFetchResult() {
        return resultLiveData;
    }

    public TeamListViewModel() {
        teamUpdateObserver = (teamList) -> updateTeamData(teamList);
        teamRemoveObserver = (team) -> removeTeamData(team);
        contactRepo.registerTeamUpdateObserver(teamUpdateObserver, true);
        contactRepo.registerTeamRemoveObserver(teamRemoveObserver, true);
    }

    public void fetchTeamList() {
        fetchResult.setStatus(LoadStatus.Loading);
        resultLiveData.postValue(fetchResult);
        contactRepo.fetchMyTeamList(new FetchCallback<List<Team>>() {
            @Override
            public void onSuccess(List<Team> param) {
                teamBeanList.clear();
                if (param != null && param.size() > 0) {
                    fetchResult.setStatus(LoadStatus.Success);
                    for (Team teamInfo : param) {
                        ContactTeamBean teamBean = new ContactTeamBean(teamInfo);
                        teamBeanList.add(teamBean);
                    }
                    fetchResult.setData(teamBeanList);
                } else {
                    fetchResult.setData(null);
                    fetchResult.setStatus(LoadStatus.Success);
                }
                resultLiveData.postValue(fetchResult);
            }

            @Override
            public void onFailed(int code) {
                fetchResult.setError(code, "");
                resultLiveData.postValue(fetchResult);
            }

            @Override
            public void onException(@Nullable Throwable exception) {
                fetchResult.setError(-1, "");
                resultLiveData.postValue(fetchResult);
            }
        });
    }

    private void removeTeamData(Team teamInfo) {
        if (teamInfo != null) {
            List<ContactTeamBean> remove = new ArrayList<>();
            for (ContactTeamBean bean : teamBeanList) {
                if (TextUtils.equals(teamInfo.getId(), bean.data.getId())) {
                    remove.add(bean);
                    break;
                }
            }

            if (remove.size() > 0) {
                fetchResult.setFetchType(FetchResult.FetchType.Remove);
                fetchResult.setData(remove);
                resultLiveData.postValue(fetchResult);
            } else {
                fetchTeamList();
            }
        }

    }

    private void updateTeamData(List<Team> teamInfoList) {
        if (teamInfoList != null && !teamInfoList.isEmpty()) {
            List<ContactTeamBean> add = new ArrayList<>();
            for (Team teamInfo : teamInfoList) {
                boolean has = false;
                for (ContactTeamBean bean : teamBeanList) {
                    if (TextUtils.equals(teamInfo.getId(), bean.data.getId())) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    ContactTeamBean teamBean = new ContactTeamBean(teamInfo);
                    add.add(teamBean);
                    teamBeanList.add(0, teamBean);
                }
            }

            if (add.size() > 0) {
                fetchResult.setFetchType(FetchResult.FetchType.Add);
                fetchResult.setData(add);
                resultLiveData.postValue(fetchResult);
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        contactRepo.registerTeamUpdateObserver(teamUpdateObserver, false);
        contactRepo.registerTeamRemoveObserver(teamRemoveObserver, false);

    }
}
