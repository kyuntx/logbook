/**
 * 
 */
package logbook.server.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logbook.data.context.GlobalContext;
import logbook.dto.BattleDto;
import logbook.dto.DeckMissionDto;
import logbook.dto.DockDto;
import logbook.dto.ItemDto;
import logbook.dto.MaterialDto;
import logbook.dto.NdockDto;
import logbook.dto.QuestDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipInfoDto;
import logbook.internal.Item;

import org.eclipse.swt.widgets.Display;

/**
 * @author Koji Ueno
 *
 */
public class QueryHandler extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2833563128459893536L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //
        //String qid_str = req.getParameter("qid");
        //System.out.println("qid=" + qid_str);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        JsonWriter writer = Json.createWriter(resp.getOutputStream());
        if (req.getRequestURI().endsWith("query")) {
            writer.writeObject(createQueryRespons());
        }
        else { // battle
            writer.writeObject(createBattleRespons());
        }
        writer.close();
    }

    private static JsonObjectBuilder shipToJson(ShipDto ship) {
        JsonArrayBuilder slot_array = Json.createArrayBuilder();
        for (Long item_number : ship.getRawSlot()) {
            slot_array.add(item_number);
        }
        return Json.createObjectBuilder()
                .add("id", ship.getId())
                .add("ship_id", ship.getShipId())
                .add("char_id", ship.getCharId())
                .add("ship_type", ship.getShipInfo().getStype())
                .add("level", ship.getLv())
                .add("cond", ship.getCond())
                .add("cond_clear_time", ship.getCondClearTime().getTimeInMillis())
                .add("bull", ship.getBull())
                .add("bull_max", ship.getBullMax())
                .add("fuel", ship.getFuel())
                .add("fuel_max", ship.getFuelMax())
                .add("now_hp", ship.getNowhp())
                .add("max_hp", ship.getMaxhp())
                .add("dock_time", ship.getDocktime())
                .add("slot_num", ship.getSlotNum())
                .add("slot_item", slot_array)
                .add("name", ship.getName());
    }

    private static JsonObjectBuilder itemToJson(ItemDto item) {
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("item_id", item.getSlotitemId());
    }

    private static JsonObjectBuilder itemInfoToJson(ItemDto item) {
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("name", item.getName())
                .add("type", item.getTypeId2());
    }

    private static JsonObjectBuilder questToJson(QuestDto item) {
        return Json.createObjectBuilder()
                .add("no", item.getNo())
                .add("page", item.getPage())
                .add("pos", item.getPos())
                .add("title", item.getTitle())
                .add("state", item.getState());
    }

    private static JsonObject createQueryRespons() {
        final JsonObjectBuilder jb = Json.createObjectBuilder();
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                { // 資源量を配列で追加
                    JsonArrayBuilder materials_array = Json.createArrayBuilder();
                    MaterialDto dto = GlobalContext.getMaterial();
                    if (dto != null) {
                        materials_array.add(dto.getFuel()); // 燃料
                        materials_array.add(dto.getAmmo()); // 弾薬
                        materials_array.add(dto.getMetal()); // 鋼材
                        materials_array.add(dto.getBauxite()); // ボーキ
                        materials_array.add(dto.getBurner()); // 高速建造材
                        materials_array.add(dto.getBucket()); // 高速修理材
                        materials_array.add(dto.getResearch()); // 開発資源
                        jb.add("materials", materials_array);
                    }
                }

                { // 艦娘リストを配列で追加
                    JsonArrayBuilder ship_array = Json.createArrayBuilder();
                    for (ShipDto ship : GlobalContext.getShipMap().values()) {
                        ship_array.add(shipToJson(ship));
                    }
                    jb.add("ships", ship_array);
                }

                {// 艦隊情報を配列で追加
                    JsonArrayBuilder dock_array = Json.createArrayBuilder();
                    for (int i = 0; i < 4; i++) {
                        DockDto dock = GlobalContext.getDock(Integer.toString(i + 1));
                        if (dock != null) {
                            JsonArrayBuilder dock_ship_array = Json.createArrayBuilder();
                            for (ShipDto ship : dock.getShips()) {
                                dock_ship_array.add(ship.getId());
                            }
                            dock_array.add(Json.createObjectBuilder()
                                    .add("name", dock.getName())
                                    .add("ships", dock_ship_array));
                        }
                    }
                    jb.add("dock", dock_array);
                }

                { // 入渠ドック情報
                    JsonArrayBuilder ndock_root = Json.createArrayBuilder();
                    for (NdockDto ndock : GlobalContext.getNdocks()) {
                        JsonArrayBuilder ndock_ship = Json.createArrayBuilder();
                        if (ndock.getNdockid() != 0) {
                            ndock_ship.add(ndock.getNdockid());
                            ndock_ship.add(ndock.getNdocktime().getTime());
                        }
                        else {
                            ndock_ship.add(-1);
                            ndock_ship.add(0);
                        }
                        ndock_root.add(ndock_ship);
                    }
                    jb.add("ndock", ndock_root);
                }

                { // 遠征情報
                    JsonArrayBuilder mission_root = Json.createArrayBuilder();
                    for (DeckMissionDto mission : GlobalContext.getDeckMissions()) {
                        JsonArrayBuilder mission_item = Json.createArrayBuilder();
                        Date comp_time = mission.getTime();
                        if (comp_time != null) {
                            mission_item.add(mission.getMissionId());
                            mission_item.add(comp_time.getTime());
                        }
                        else {
                            mission_item.add(-1);
                            mission_item.add(0);
                        }
                        mission_root.add(mission_item);
                    }
                    jb.add("mission", mission_root);
                }

                { // 装備
                    JsonArrayBuilder item_array = Json.createArrayBuilder();
                    for (String itemid : Item.keySet()) {
                        item_array.add(itemInfoToJson(Item.get(itemid)));
                    }
                    jb.add("master_items", item_array);
                }

                { // 装備
                    JsonArrayBuilder item_array = Json.createArrayBuilder();
                    for (ItemDto item : GlobalContext.getItemMap().values()) {
                        item_array.add(itemToJson(item));
                    }
                    jb.add("items", item_array);
                }

                { // クエスト
                    JsonArrayBuilder quest_array = Json.createArrayBuilder();
                    for (QuestDto quest : GlobalContext.getQuest()) {
                        if (quest == null)
                            continue;
                        quest_array.add(questToJson(quest));
                    }
                    jb.add("quest", quest_array);
                    jb.add("num_quest", GlobalContext.getQuest().size());
                }

                { // 出撃
                    JsonArrayBuilder sortie = Json.createArrayBuilder();
                    for (boolean mission : GlobalContext.getIsSortie()) {
                        sortie.add(mission);
                    }
                    jb.add("sortie", sortie);
                }

                { // 出撃数, 遠征数
                    JsonArrayBuilder game_record = Json.createArrayBuilder();
                    for (int rec : GlobalContext.getGameRecord()) {
                        game_record.add(rec);
                    }
                    jb.add("game_record", game_record);
                }
            }
        });

        return jb.build();
    }

    private static JsonObject createBattleRespons() {
        final JsonObjectBuilder jb = Json.createObjectBuilder();
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                BattleDto battleDto = GlobalContext.getLastBattleDto();
                boolean isSortie = false;
                for (boolean sortie : GlobalContext.getIsSortie()) {
                    if (sortie) {
                        isSortie = true;
                        break;
                    }
                }

                if (isSortie) {
                    {// 戦闘中のマップ
                        JsonArrayBuilder map_array = Json.createArrayBuilder();
                        int[] sortieMap = GlobalContext.getSortieMap();
                        map_array.add(sortieMap[0]);
                        map_array.add(sortieMap[1]);
                        map_array.add(sortieMap[2]);
                        jb.add("map", map_array);
                    }
                    if (battleDto != null) {// HP
                        JsonArrayBuilder fship_array = Json.createArrayBuilder();
                        JsonArrayBuilder eship_array = Json.createArrayBuilder();

                        List<ShipDto> fships = battleDto.getDock().getShips();
                        for (ShipDto ship : fships) {
                            fship_array.add(shipToJson(ship));
                        }

                        List<ShipInfoDto> eships = battleDto.getEnemy();
                        int[] enowhp = battleDto.getNowEnemyHp();
                        int[] emaxhp = battleDto.getMaxEnemyHp();
                        for (int i = 0; i < eships.size(); ++i) {
                            ShipInfoDto ship = eships.get(i);
                            String flagship = ship.getFlagship();
                            int level = (flagship.equals("flagship") ? 2
                                    : flagship.equals("elite") ? 1
                                            : 0);
                            eship_array.add(Json.createObjectBuilder()
                                    .add("ship_id", ship.getShipId())
                                    .add("ship_type", ship.getStype())
                                    .add("level", level)
                                    .add("now_hp", enowhp[i])
                                    .add("max_hp", emaxhp[i]));
                        }

                        jb.add("friend", fship_array);
                        jb.add("enemy", eship_array);
                    }
                }
            }
        });

        return jb.build();
    }
}
