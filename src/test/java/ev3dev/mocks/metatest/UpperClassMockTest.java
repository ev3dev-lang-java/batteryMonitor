package ev3dev.mocks.metatest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public @Slf4j class UpperClassMockTest {

    @Mock
    InsideClass insideClass;

    @InjectMocks
    UpperClass upperClass;

    @Test
    public void test() throws Exception {

        given(insideClass.methodA()).willReturn(20);

        final int result = upperClass.methodA();
        assertThat(result, is(40));

        verify(insideClass, times(1)).methodA();

    }

}
