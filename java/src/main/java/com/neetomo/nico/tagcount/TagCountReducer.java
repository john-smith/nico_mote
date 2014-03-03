package com.neetomo.nico.tagcount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.mapreduce.Reducer;

import java.util.Iterator;
import java.io.IOException;

public class TagCountReducer extends Reducer<Text, IntWritable, NullWritable, Text> {
    private Text outputValue = new Text();
    /** 足切りのための最低値 */
    private final int THRESHOLD = 10;

    @Override
    public void setup(Context context) 
        throws IOException, InterruptedException{
        outputValue.set("tag,time,count");
        context.write(NullWritable.get(), outputValue);
    }

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException{
        int sum = 0;

        for(IntWritable i : values) {
            sum += i.get();
        }

        if (sum > THRESHOLD) {
            outputValue.set(key.toString() + "," + sum);
            context.write(NullWritable.get(), outputValue);
        }
    }
}
