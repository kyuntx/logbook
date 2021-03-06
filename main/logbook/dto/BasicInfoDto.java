/**
 * 
 */
package logbook.dto;

import javax.json.JsonObject;

/**
 * @author Nekopanda
 *
 */
public class BasicInfoDto {

    private String nickname;
    private long memberId;
    private int deckCount;
    private int kdockCount;
    private int ndockCount;
    private int missionCount;
    private int missionSuccess;
    private int practiceWin;
    private int practiceLose;
    private int sortieWin;
    private int sortieLose;

    public BasicInfoDto(JsonObject data) {
        this.setNickname(data.getString("api_nickname"));
        this.setMemberId(Long.valueOf(data.getString("api_member_id")));
        this.setDeckCount(data.getInt("api_count_deck"));
        this.setKdockCount(data.getInt("api_count_kdock"));
        this.setNdockCount(data.getInt("api_count_ndock"));
        this.setMissionCount(data.getInt("api_ms_count"));
        this.setMissionSuccess(data.getInt("api_ms_success"));
        this.setPracticeWin(data.getInt("api_pt_win"));
        this.setPracticeLose(data.getInt("api_pt_lose"));
        this.setSortieWin(data.getInt("api_st_win"));
        this.setSortieLose(data.getInt("api_st_lose"));
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * @param nickname セットする nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return memberId
     */
    public long getMemberId() {
        return this.memberId;
    }

    /**
     * @param memberId セットする memberId
     */
    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    /**
     * @return deckCount
     */
    public int getDeckCount() {
        return this.deckCount;
    }

    /**
     * @param deckCount セットする deckCount
     */
    public void setDeckCount(int deckCount) {
        this.deckCount = deckCount;
    }

    /**
     * @return kdockCount
     */
    public int getKdockCount() {
        return this.kdockCount;
    }

    /**
     * @param kdockCount セットする kdockCount
     */
    public void setKdockCount(int kdockCount) {
        this.kdockCount = kdockCount;
    }

    /**
     * @return ndockCount
     */
    public int getNdockCount() {
        return this.ndockCount;
    }

    /**
     * @param ndockCount セットする ndockCount
     */
    public void setNdockCount(int ndockCount) {
        this.ndockCount = ndockCount;
    }

    /**
     * @return missionCount
     */
    public int getMissionCount() {
        return this.missionCount;
    }

    /**
     * @param missionCount セットする missionCount
     */
    public void setMissionCount(int missionCount) {
        this.missionCount = missionCount;
    }

    /**
     * @return missionSuccess
     */
    public int getMissionSuccess() {
        return this.missionSuccess;
    }

    /**
     * @param missionSuccess セットする missionSuccess
     */
    public void setMissionSuccess(int missionSuccess) {
        this.missionSuccess = missionSuccess;
    }

    /**
     * @return practiceWin
     */
    public int getPracticeWin() {
        return this.practiceWin;
    }

    /**
     * @param practiceWin セットする practiceWin
     */
    public void setPracticeWin(int practiceWin) {
        this.practiceWin = practiceWin;
    }

    /**
     * @return practiceLose
     */
    public int getPracticeLose() {
        return this.practiceLose;
    }

    /**
     * @param practiceLose セットする practiceLose
     */
    public void setPracticeLose(int practiceLose) {
        this.practiceLose = practiceLose;
    }

    /**
     * @return sortieWin
     */
    public int getSortieWin() {
        return this.sortieWin;
    }

    /**
     * @param sortieWin セットする sortieWin
     */
    public void setSortieWin(int sortieWin) {
        this.sortieWin = sortieWin;
    }

    /**
     * @return sortieLose
     */
    public int getSortieLose() {
        return this.sortieLose;
    }

    /**
     * @param sortieLose セットする sortieLose
     */
    public void setSortieLose(int sortieLose) {
        this.sortieLose = sortieLose;
    }
}
