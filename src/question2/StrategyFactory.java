/*****************************************************************************

 File        : StrategyFactory.java

 Date        :22/01/2020

 Description : Java class with a single method to convert a string into a
               strategy

 Author      : 100239986

 ******************************************************************************/
package question2;

public class StrategyFactory {

    public static Strategy setStrategy(String input){
        Strategy strategy;
        input=input.toLowerCase();
        switch(input){
            case "b": case "basic": case "basicstrategy":
                strategy = new BasicStrategy();
                break;
            case "h": case "human": case "humanstrategy":
                strategy = new HumanStrategy();
                break;
            case "t": case "thinker": case "thinkerstrategy":
                strategy = new ThinkerStrategy();
                break;
            case "m": case "my": case "mystrategy":
                strategy =new MyStrategy();
                break;
            default:
                strategy = null;
        }
        return strategy;
    }
}
