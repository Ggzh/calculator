import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CalculatorV2 {

    //操作链
    private static Map<Integer, Node> nodeMap = new HashMap<>();

    //最新计算结果
    private static BigDecimal result;

    //递增事务id
    public static AtomicInteger curZId = new AtomicInteger(0);

    //redo/undo的位置
    private static Integer operISymbolndex = 0;

    public static void main(String[] args) {
        Node node = new Node();
        node.num1 = BigDecimal.ONE;
        node.symbol = "+";
        node.num2 = new BigDecimal(2);
        calcul(node);
        node.symbol = "-";
        node.num2 = new BigDecimal(10);
        calcul(node);
        redo();
        undo();
        undo();
        undo();
        redo();
        node.symbol = "-";
        node.num2 = new BigDecimal(2);
        calcul(node);

    }

    public static void redo() {
        if (operISymbolndex <= 0) {
            operISymbolndex = 1;
        }
        Node redoNode = nodeMap.get(operISymbolndex);
        BigDecimal oldResult = result;
        result = redoNode.value;
        operISymbolndex++;
        if (operISymbolndex > nodeMap.size()) {
            operISymbolndex = nodeMap.size();
        }
        System.out.println("执行redo操作，历史值：" + oldResult + "，新值：" + result);
    }

    public static void undo() {

        if (operISymbolndex <= 0) {
            System.out.println("不能再undo操作");
            return;
        }
        operISymbolndex--;
        BigDecimal oldResult = result;
        if (operISymbolndex == 0) {
            result = BigDecimal.ZERO;
        } else {
            Node redoNode = nodeMap.get(operISymbolndex);
            result = redoNode.value;
            if (operISymbolndex < 0) {
                operISymbolndex = 0;
            }
        }

        System.out.println("执行undo操作，历史值：" + oldResult + "，新值：" + result);
    }

    public static void calcul(Node node) {
        if (result != null) {
            node.num1 = result;
        }
        node.zId = curZId.addAndGet(1);
        switch (node.symbol) {
            case "+":
                node.value = node.num1.add(node.num2);
                break;
            case "-":
                node.value = node.num1.subtract(node.num2).setScale(2, RoundingMode.HALF_UP);
                break;
            case "*":
                node.value = node.num1.multiply(node.num2).setScale(2, RoundingMode.HALF_UP);
                break;
            case "/":
                node.value = node.num1.divide(node.num2, RoundingMode.HALF_UP);
                break;
        }
        nodeMap.put(node.zId, new Node(node.zId, node.symbol, node.num1, node.num2, node.value));
        operISymbolndex++;
        result = node.value;
        System.out.println(node.toString());
    }

    static class Node {
        //事务id
        private Integer zId;

        //操作符：+、-、*、/
        private String symbol;
        //参数1
        private BigDecimal num1;

        //参数2
        private BigDecimal num2;

        //当前操作结果
        private BigDecimal value;

        public Node() {

        }

        public Node(Integer zId, String symbol, BigDecimal num1, BigDecimal num2, BigDecimal value) {
            this.zId = zId;
            this.symbol = symbol;
            this.num1 = num1;
            this.num2 = num2;
            this.value = value;
        }

        public Integer getzId() {
            return zId;
        }

        public void setzId(Integer zId) {
            this.zId = zId;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }


        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigDecimal getNum1() {
            return num1;
        }

        public void setNum1(BigDecimal num1) {
            this.num1 = num1;
        }

        public BigDecimal getNum2() {
            return num2;
        }

        public void setNum2(BigDecimal num2) {
            this.num2 = num2;
        }

        @Override
        public String toString() {
            return num1 + " " + symbol + " " + num2 + " = " + value;
        }
    }

}
