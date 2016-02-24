package jfditests.logictests;

import jfdi.logic.ControlCenter;
import jfdi.logic.commands.ListCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ControlCenter.class, ListCommand.class})
public class ControlCenterTest {

    @Mock private ListCommand.Builder builder;
    @Mock private ListCommand ls;

    private ControlCenter cc = ControlCenter.getInstance();

    @Before
    public void setUp() throws Exception {
        // Mocking ListCommand.Builder.
        when(builder.build()).thenReturn(ls);
        when(builder.addTag(anyString())).thenReturn(builder);
        when(builder.addTags(any())).thenReturn(builder);
        whenNew(ListCommand.Builder.class).withNoArguments().thenReturn(builder);

        // Mocking ListCommand static methods.
        // i.e. addSuccessHook and addFailureHook
        mockStatic(ListCommand.class);
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(cc, ControlCenter.getInstance());
        assertSame(ControlCenter.getInstance(), ControlCenter.getInstance());
    }

    @Test
    public void testHandleInput() throws Exception {
        // TODO: Update when Parser is ready
        // Right now this is an example of using Mockito
        cc.handleInput("anything");

        InOrder inorder = inOrder(builder);

        inorder.verify(builder).addTag("important");
        inorder.verify(builder).addTag("urgent");
        inorder.verify(builder).addTag("personal");
        inorder.verify(builder).addTags(any());
        inorder.verify(builder).build();
        verifyNoMoreInteractions(builder);

        verify(ls).execute();
        verifyNoMoreInteractions(ls);
    }

    @Test
    public void testMain() throws Exception {
        ControlCenter.main(new String[] {});

        // This is the way to verify that a static method has been called.
        verifyStatic(times(1));
        ListCommand.addSuccessHook(any());

        // Each static method call must be prepended by a `verifyStatic()`.
        verifyStatic(times(1));
        ListCommand.addFailureHook(any());
    }

}
