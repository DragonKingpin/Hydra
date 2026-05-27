package com.walnut.odin.proc;

import java.net.URI;
import java.util.Map;

import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.ArchExecutionImage;
import com.pinecone.hydra.proc.image.ImageLoader;

public class RemoteSurrogateExecutionImage extends ArchExecutionImage {

    protected static final String IMAGE_SIGNATURE = RemoteSurrogateExecutionImage.class.getSimpleName();

    public RemoteSurrogateExecutionImage( String imageAddress, ImageLoader imageLoader ) {
        super(
                imageAddress,
                new RemoteSurrogateEntryPoint( imageAddress ),
                MediatedRemoteProcess.class,
                URI.create( "red://" + imageAddress ),
                imageLoader,
                IMAGE_SIGNATURE,
                ControllableLevel.Monitor
        );
    }

    protected static class RemoteSurrogateEntryPoint extends ArchEntryPointRunnable {

        protected String mszImageAddress;

        public RemoteSurrogateEntryPoint( String imageAddress ) {
            this.mszImageAddress = imageAddress;
        }

        @Override
        public int main( Map<String, String> args ) {
            throw new UnsupportedOperationException(
                    "Remote surrogate image cannot be executed locally: " + this.mszImageAddress
            );
        }
    }
}
