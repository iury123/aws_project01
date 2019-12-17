package br.com.miguel.aws_lambda03;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import java.util.UUID;

public class S3EventJob implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Starting execution...");
        return null;
    }

    private void saveInvoice(Invoice invoice) {
        final AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(
                new EnvironmentVariableCredentialsProvider());
        ddbClient.withRegion(Regions.US_EAST_1);
        DynamoDB dynamoDB = new DynamoDB(ddbClient);
        Table table = dynamoDB.getTable("invoice-lambda");
        table.putItem(new PutItemSpec().withItem(new Item()
                .withString("id", UUID.randomUUID().toString())
                .withString("invoiceNumber", invoice.getInvoiceNumber())
                .withString("customerName", invoice.getCustomerName())
                .withFloat("totalValue", invoice.getTotalValue())
                .withLong("productId", invoice.getProductId())
                .withInt("quantity", invoice.getQuantity())));
    }
}