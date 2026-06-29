package com.walnut.odin.proc;

import java.net.URI;
import java.util.Map;

import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.ArchExecutionImage;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageLoader;

public class RemoteSurrogateExecutionImage extends ArchExecutionImage {

    protected static final String IMAGE_SIGNATURE = RemoteSurrogateExecutionImage.class.getSimpleName();

    protected ExecutionImage mResolvedImage;

    public RemoteSurrogateExecutionImage( String imageAddress, ImageLoader imageLoader ) {
        this( imageAddress, imageLoader, null );
    }

    public RemoteSurrogateExecutionImage( String imageAddress, ImageLoader imageLoader, ExecutionImage resolvedImage ) {
        super(
                imageAddress,
                () -> new RemoteSurrogateEntryPoint( imageAddress ),
                MediatedRemoteProcess.class,
                URI.create( "red://" + imageAddress ),
                imageLoader,
                IMAGE_SIGNATURE,
                ControllableLevel.Monitor
        );
        this.mResolvedImage = resolvedImage;
    }

    public ExecutionImage getResolvedImage() {
        return this.mResolvedImage;
    }

    public boolean hasResolvedImage() {
        return this.mResolvedImage != null;
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
