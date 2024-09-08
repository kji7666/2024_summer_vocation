import java.util.*;

// 遊戲進行流程
/**
 * 1. 洗牌 : Poker
 * 2. 發牌 : Poker
 * 3. 下注 or 蓋牌 : Player
 * 4. 發公牌3張 : Poker
 * 5. 下注 or 蓋牌 : Player
 * 6. 發公牌1張 : Poker
 * 7. 下注 or 蓋牌 : Player
 * 8. 發公牌1張 : Poker
 * 9. 開牌 : Poker
 * 10. 判斷 : Poker
 */
/* 
public class Poker {
    private Card[] deck; // 0~51
    private int deckTop; // init = 51
    private List<Card> publicCard; // 公牌
    private List<Player> players;
    private final int initChips = 1000;
    private int chipsPool;
    private Map<String, Integer> cardTypeLevel; 

    public Poker(int playersCount) {
        shuffle();
        chipsPool = 0;
        deckTop = 51;
        publicCard = new ArrayList<>();
        players = new ArrayList<>();
        for(int i=0; i<playersCount; i++){
            Card[] hand = new Card[2];
            hand[0] = drawCard();
            hand[1] = drawCard();
            players.add(new Player(i, hand, initChips));
        }
        setMap(cardTypeLevel);
    }

    private void setMap(Map<String, Integer> map){
        map.put("同花順", 9);
        map.put("四條", 8);
        map.put("葫蘆", 7);
        map.put("同花", 6);
        map.put("順子", 5);
        map.put("三條", 4);
        map.put("兩對", 3);
        map.put("一對", 2);
        map.put("散牌", 1);
    }


    // 洗牌
    public void shuffle() {
        String[] colors = new String[]{"梅花", "菱形", "愛心", "黑桃"};
        String[] numbers = new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        // 
    }
    // 抽一張牌
    public Card drawCard() {
        if(deckTop < 0) {
            throw new IllegalArgumentException("牌堆已無牌");
        }
        return deck[deckTop --];
    }

    // 發公牌
    public void issuePublicCard(int count) {
        for(int i=0; i<count; i++) {
            if(publicCard.size() == 5){
                break;
            }
            publicCard.add(drawCard());
        }
    }
    // 亮牌
    public Player showCards() {
        Player winner;
        int maxLevel = 0;
        for(Player player : players) {
            if(!player.isFold()) {
                String result = judgeResult(publicCard, player.getHand());
                int cardLevel = cardTypeLevel.get(result);
                if(cardLevel > maxLevel) {
                    winner = player;
                    maxLevel = cardLevel;
                } else if{
                    // 
                }
            }
        }

        return winner;
    }

    public int judgeResult(List<Card> publicCards, Card[] hand) {
        if(isFlush(playerCards)){
            return 1;
        } else if (isFour(playerCards)){
            return 2;
        } else if (isFullHouse(playerCards)){
            return 3;
        } else if (isSameColor(playerCards)){
            return 4;
        } else if (isStraight(playerCards)){
            return 5;
        } else if (isSanjo(playerCards)){
            return 6;
        } else if (isTwoPair(playerCards)){
            return 7;
        } else if (isPair(playerCards)){
            return 8;
        } else {
            return 9;
        }
    }

    private boolean isSameColor(List<Card> playerCards){
        boolean isSameColor = true;
        for(int whichCard = 1; whichCard < 5; whichCard ++){
            if(!playerCards.get(whichCard).getColor().equals(playerCards.get(whichCard - 1).getColor())){
                isSameColor = false;
                break;
            }
        }
        if(isSameColor){
            return true;
        } else {
            return false;
        }
    }
    private boolean isStraight(List<Card> playerCards){
        boolean isStraight = true;
        for(int whichCard = 1; whichCard < 5; whichCard ++){
            if(!(playerCards.get(whichCard).pointToValue() == playerCards.get(whichCard-1).pointToValue() + 1)){
                isStraight = false;
                break;
            }
        }
        if(isStraight){
            return true;
        } else {
            return false;
        }
    }
    private boolean isFlush(List<Card> playerCards){
        if(isStraight(playerCards) && isSameColor(playerCards)){
            return true;
        } else {
            return false;
        }
    }
    private boolean isFour(List<Card> playerCards){
        if(mostCountSamePoint(playerCards) == 4){
            return true;
        } else {
            return false;
        }
    }
    private boolean isFullHouse(List<Card> playerCards){
        boolean isFullHouse = true;
        if(!playerCards.get(0).getPoint().equals(playerCards.get(1).getPoint())){
            isFullHouse = false;
        }
        if(!playerCards.get(3).getPoint().equals(playerCards.get(4).getPoint())){
            isFullHouse = false;
        }
        if(!playerCards.get(1).getPoint().equals(playerCards.get(2).getPoint()) && !playerCards.get(2).getPoint().equals(playerCards.get(3).getPoint())){
            isFullHouse = false;
        }
        if(isFullHouse){
            return true;
        } else {
            return false;
        }
    }
    private boolean isSanjo(List<Card> playerCards){
        if(mostCountSamePoint(playerCards) == 3){
            return true;
        } else {
            return false;
        }
    }
    private boolean isTwoPair(List<Card> playerCards){
        int numberOfPair = 0;
        for(int whichCard = 1; whichCard < 5; whichCard ++){
            if(playerCards.get(whichCard).getPoint().equals(playerCards.get(whichCard-1).getPoint())){
                numberOfPair ++;
            }
        }
        if(numberOfPair == 2){
            return true;
        } else {
            return false;
        }
    }
    private boolean isPair(List<Card> playerCards){ // 
        if(mostCountSamePoint(playerCards) == 2){
            return true;
        } else {
            return false;
        }
    }
    private int mostCountSamePoint(List<Card> playerCards){
        int mostCountSamePoint = 0;
        int countSamePoint = 1;
        for(int whichCard = 1; whichCard < 5; whichCard ++){
            if(playerCards.get(whichCard).getPoint().equals(playerCards.get(whichCard-1).getPoint())){
                countSamePoint ++;
                mostCountSamePoint = Math.max(mostCountSamePoint, countSamePoint);
            } else {
                countSamePoint = 1;
            }
        }
        //System.out.println(mostCountSamePoint);
        return mostCountSamePoint;
    }
}



class Card {
    private String color; // 梅花/菱形/愛心/黑桃
    private String number; // 1~13

    public Card(String color, String number) {
        this.color = color;
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }
}

class Player {
    private int playerCode;
    private boolean isFold; // 是否蓋牌
    private Card[] hand; // 手牌2張
    private int chips; // 籌碼

    public Player(int playerCode, Card[] hand, int chips) {
        this.playerCode = playerCode;
        this.hand = hand;
        this.chips = chips;
    }

    public int getPlayerCode() {
        return playerCode;
    }

    public boolean isFold() {
        return isFold;
    }

    public void setFold(boolean isFold) {
        this.isFold = isFold;
    }

    public Card[] getHand() {
        return hand;
    }

    public int getChips() {
        return chips;
    }
    
    // 下注
    public void placeABet(int chips) {
        if(this.chips < chips) {
            throw new IllegalArgumentException("剩餘籌碼不足");
        }
        this.chips -= chips;
    }
}

// gui
class PokerGui {

}
*/