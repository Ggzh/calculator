import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalculatorV1 {
    //结果
    BigDecimal sumValu = null;
    //当前操作
    String curSymbol;
    //操作符记录
    List<String> symbolList = new ArrayList<>();
    //操作后的结果集
    List<BigDecimal> sumList = new ArrayList<>();
    //参数列表
    List<BigDecimal> paramList = new ArrayList<>();
    //回滚次数
    int operISymbolndex = -1;

    public static void main(String[] args) {
        CalculatorV1 calculatorV1 = new CalculatorV1();
        calculatorV1.setValue(new BigDecimal(2));
        calculatorV1.setCurSymbol("+");
        calculatorV1.setValue(new BigDecimal(1));
        calculatorV1.calcu();
        calculatorV1.setCurSymbol("-");
        calculatorV1.setValue(new BigDecimal(4));
        calculatorV1.calcu();
        calculatorV1.undo();
//        calculatorV1.undo();
        calculatorV1.redo();
//        calculatorV1.undo();
//        calculatorV1.redo();
//        calculatorV1.redo();
//        calculatorV1.setCurSymbol("*");
//        calculatorV1.setValue(BigDecimal.ZERO);
//        calculatorV1.calcu();

    }

    public void redo() {
        if(operISymbolndex==0){
            System.out.println("无法redo");
            return;
        }
        operISymbolndex++;
        if (operISymbolndex > symbolList.size()) {
            operISymbolndex = symbolList.size();
            System.out.println("redo的操作：" + symbolList.get(operISymbolndex-1) + paramList.get(operISymbolndex) + ", redo前的值：" + sumList.get(operISymbolndex - 1) + ", redo后的值：" + sumList.get(operISymbolndex - 1));
        } else {
            BigDecimal oldValue = sumValu;
            curSymbol = symbolList.get(operISymbolndex-1);
            sumValu = sumList.get(operISymbolndex-2);
            System.out.println("redo操作开始");
            calcu();
            System.out.println("redo操作结束：" + curSymbol + paramList.get(operISymbolndex) + ", redo前的值：" + oldValue + ", redo后的值：" + sumValu);
        }

    }

    public void undo() {
        if(operISymbolndex==-1){
            operISymbolndex=symbolList.size();
        }

        if (operISymbolndex == 0) {
            System.out.println("已回到初始化节点，无法继续回滚");
            return;
        }
        operISymbolndex--;
        if(operISymbolndex==0){
            sumValu = BigDecimal.ZERO;
            System.out.println("已回到初始化节点0");
            return;
        }
        BigDecimal oldValue = sumValu;
        curSymbol = symbolList.get(operISymbolndex-1);
        sumValu = sumList.get(operISymbolndex);
        System.out.println("undo操作开始");
        calcu();
        System.out.println("undo操作结束：" + curSymbol + paramList.get(operISymbolndex+1) + ", undo前的值：" + oldValue + ", undo后的值：" + sumValu);

    }

    public void calcu() {
        BigDecimal param2 = paramList.get(paramList.size() - 1);
        BigDecimal oldSumValue = sumValu;
        switch (curSymbol) {
            case "+":
                sumValu = sumValu.add(param2);
                break;
            case "-":
                sumValu = sumValu.subtract(param2).setScale(2, RoundingMode.HALF_UP);
                break;
            case "*":
                sumValu = sumValu.multiply(param2).setScale(2, RoundingMode.HALF_UP);
                break;
            case "/":
                sumValu = sumValu.divide(param2, RoundingMode.HALF_UP);
                break;
        }
        if(sumList.size()<symbolList.size()){
            sumList.add(sumValu);
        }
        System.out.println("计算"+oldSumValue + " " + curSymbol + " " + param2 + " = " + sumValu);
    }

    public BigDecimal getSumValu() {
        return sumValu;
    }

    public void setSumValu(BigDecimal sumValu) {
        this.sumValu = sumValu;
    }


    public void setValue(BigDecimal value) {
        if (sumValu == null) {
            sumValu = value;
        }
        paramList.add(value);
    }

    public List<String> getSymbolList() {
        return symbolList;
    }

    public void setSymbolList(List<String> sumbolList) {
        this.symbolList = sumbolList;
    }

    public List<BigDecimal> getParamList() {
        return paramList;
    }

    public void setParamList(List<BigDecimal> paramList) {
        this.paramList = paramList;
    }

    public int getOperISymbolndex() {
        return operISymbolndex;
    }

    public void setOperISymbolndex(int operISymbolndex) {
        this.operISymbolndex = operISymbolndex;
    }

    public String getCurSymbol() {
        return curSymbol;
    }

    public void setCurSymbol(String curSymbol) {
        this.curSymbol = curSymbol;
        this.symbolList.add(curSymbol);
    }
}
