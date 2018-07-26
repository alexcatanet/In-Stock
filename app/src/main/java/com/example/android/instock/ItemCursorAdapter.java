package com.example.android.instock;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instock.data.InventoryContract;

public class ItemCursorAdapter extends CursorAdapter {

   ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView mItemName = view.findViewById(R.id.item_name);
        TextView mItemPrice = view.findViewById(R.id.item_price);
        TextView mItemQuantity = view.findViewById(R.id.item_in_stock);
        ImageButton sellButton = view.findViewById(R.id.sell_button);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int stockColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);

        String itemName = cursor.getString(nameColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);
        String itemQuantity = cursor.getString(stockColumnIndex);


        String gamePriceText = "Price: " + itemPrice + " $";
        String gameQuantityText = "In stock: " + itemQuantity + " pcs";

        mItemName.setText(itemName);
        mItemPrice.setText(gamePriceText);
        mItemQuantity.setText(gameQuantityText);

        final int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry
                .COLUMN_QUANTITY);
        String currentQuantity = cursor.getString(quantityColumnIndex);
        final int quantityIntCurrent = Integer.valueOf(currentQuantity);

        final int productId = cursor.getInt(cursor.getColumnIndex(InventoryContract
                .InventoryEntry._ID));

        sellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (quantityIntCurrent > 0) {
                    int newQuantity = quantityIntCurrent - 1;
                    Uri quantityUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry
                            .CONTENT_URI, productId);

                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantity);
                    context.getContentResolver().update(quantityUri, values, null,
                            null);
                } else {
                    Toast.makeText(context, "This item is out of stock!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
