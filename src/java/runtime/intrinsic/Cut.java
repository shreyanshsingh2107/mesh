/**
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2009-2013 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute
 * this file in accordance with the terms of the MIT license,
 * a copy of which can be found in the LICENSE.txt file or at
 * http://opensource.org/licenses/MIT.
 */
package runtime.intrinsic;

import compile.type.Type;
import compile.type.TypeParam;
import compile.type.Types;
import runtime.rep.lambda.IntrinsicLambda;
import runtime.rep.list.ListValue;
import runtime.rep.list.PersistentList;
import runtime.rep.Tuple;

import java.util.Iterator;

/**
 * cut(list, indexes) returns list cuts starting at indexes.
 * Out of range indexes will throw, currently.
 *
 * @author Basil Hosmer
 */
public final class Cut extends IntrinsicLambda
{
    public static final String NAME = "cut";

    private static final Type T = new TypeParam("T");
    private static final Type LIST_T = Types.list(T);
    private static final Type LIST_INT = Types.list(Types.INT);
    private static final Type LIST_LIST_T = Types.list(LIST_T);

    public static final Type TYPE = Types.fun(Types.tup(LIST_T, LIST_INT), LIST_LIST_T);

    public String getName()
    {
        return NAME;
    }

    public Type getType()
    {
        return TYPE;
    }

    public Object apply(final Object arg)
    {
        final Tuple args = (Tuple)arg;
        return invoke((ListValue)args.get(0), (ListValue)args.get(1));
    }

    public static ListValue invoke(final ListValue list, final ListValue indexes)
    {
        final PersistentList result =
            PersistentList.alloc(indexes.size());

        final Iterator<?> iter = indexes.iterator();

        if (iter.hasNext())
        {
            int i = 0;
            int start = (Integer)iter.next();

            while (iter.hasNext())
            {
                final int end = (Integer)iter.next();
                result.updateUnsafe(i++, list.subList(start, end));
                start = end;
            }

            result.updateUnsafe(i, list.subList(start, list.size()));
        }

        return result;
    }
}