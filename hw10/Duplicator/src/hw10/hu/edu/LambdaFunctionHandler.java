package hw10.hu.edu;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<String,String> {

    @Override
    public String handleRequest(String input, Context context) {
        context.getLogger().log(input + input);
        
		return input+input;        
    }
}
