package application.entities;

import com.weilerhaus.productKeys.ProductKeyGenerator;
import com.weilerhaus.productKeys.impl.workers.BasicChecksumWorker;
import com.weilerhaus.productKeys.impl.workers.BasicProductKeySectionWorker;
import com.weilerhaus.productKeys.impl.workers.BasicProductKeyStylingWorker;
import com.weilerhaus.productKeys.workers.BlacklistWorker;
import com.weilerhaus.productKeys.workers.ChecksumWorker;
import com.weilerhaus.productKeys.workers.ProductKeySectionWorker;
import com.weilerhaus.productKeys.workers.ProductKeyStylingWorker;
import com.weilerhaus.productKeys.workers.SeedAvailabilityWorker;

public class KeyEncodingGenerator extends ProductKeyGenerator<ProductKeyEncoding> {
    public KeyEncodingGenerator(final ProductKeyEncoding... productKeyEncoding) {
        super(8, productKeyEncoding);
    }

    /* PROTECTED METHDOS */
    @Override
    protected ProductKeySectionWorker buildProductKeySectionWorker() {
        return new BasicProductKeySectionWorker();
    }

    @Override
    protected ChecksumWorker buildChecksumWorker() {
        return new BasicChecksumWorker();
    }

    @Override
    protected BlacklistWorker buildBlacklistWorker() {
        return null;
    }

    @Override
    protected ProductKeyStylingWorker buildProductKeyStylingWorker() {
        return new BasicProductKeyStylingWorker();
    }

    @Override
    protected SeedAvailabilityWorker buildSeedAvailabilityWorker() {
        return null;
    }
}
