package josephcsible.infinitefluids;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class InfiniteFluidsClassTransformer implements IClassTransformer {

	private void transformUpdateTick(MethodNode mn) {
		/*
		We're trying to change this:
		if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.WATER)
		to this:
		if (this.adjacentSourceBlocks >= 2 && InfiniteFluidsHooks.shouldCreateSourceBlock(this, worldIn, pos, state, rand))

		Here's the relevant piece of the bytecode:
		L18
		LINENUMBER 70 L18
		FRAME SAME
		ALOAD 0
		GETFIELD net/minecraft/block/BlockDynamicLiquid.adjacentSourceBlocks : I
		ICONST_2 *** searched for
		IF_ICMPLT L22 *** searched for
		ALOAD 0 *** target node
		*** new stuff goes here
		GETFIELD net/minecraft/block/BlockDynamicLiquid.blockMaterial : Lnet/minecraft/block/material/Material; *** removed
		GETSTATIC net/minecraft/block/material/Material.WATER : Lnet/minecraft/block/material/Material; *** removed
		IF_ACMPNE L22 *** removed
		*/

		final String hookDesc = InfiniteFluidsLoadingPlugin.runtimeDeobfuscationEnabled ? "(Lalm;Laid;Lcm;Lars;Ljava/util/Random;)Z" : "(Lnet/minecraft/block/BlockDynamicLiquid;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)Z";
		AbstractInsnNode targetNode = null;
		for (AbstractInsnNode instruction : mn.instructions.toArray())
		{
			if (instruction.getOpcode() == ICONST_2 && instruction.getNext().getOpcode() == IF_ICMPLT)
			{
				targetNode = instruction.getNext().getNext(); // this is ALOAD 0
				break;
			}
		}
		if (targetNode == null)
		{
			System.err.println("Failed to find the part of updateTick we need to patch!");
			return;
		}
		System.out.println("Patching updateTick");
		mn.instructions.remove(targetNode.getNext()); // remove GETFIELD
		mn.instructions.remove(targetNode.getNext()); // remove GETSTATIC
		JumpInsnNode n = (JumpInsnNode)targetNode.getNext();
		LabelNode ln = n.label;
		mn.instructions.remove(n); // remove IF_ACMPNE
		InsnList toInsert = new InsnList();
		toInsert.add(new VarInsnNode(ALOAD, 1));
		toInsert.add(new VarInsnNode(ALOAD, 2));
		toInsert.add(new VarInsnNode(ALOAD, 3));
		toInsert.add(new VarInsnNode(ALOAD, 4));
		toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(InfiniteFluidsHooks.class), "shouldCreateSourceBlock", hookDesc, false));
		toInsert.add(new JumpInsnNode(IFEQ, ln));
		mn.instructions.insert(targetNode, toInsert);
	}

	private static ClassNode byteArrayToClassNode(byte[] basicClass) {
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cn, 0);
		return cn;
	}

	private static byte[] classNodeToByteArray(ClassNode cn) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cn.accept(cw);
		return cw.toByteArray();
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(!transformedName.equals("net.minecraft.block.BlockDynamicLiquid")) {
			return basicClass;
		}
		ClassNode cn = byteArrayToClassNode(basicClass);

		String updateTickName, updateTickDesc;
		if(InfiniteFluidsLoadingPlugin.runtimeDeobfuscationEnabled) {
			updateTickName = "b";
			updateTickDesc = "(Laid;Lcm;Lars;Ljava/util/Random;)V";
		} else {
			updateTickName = "updateTick";
			updateTickDesc = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V";
		}
		for(MethodNode mn : cn.methods) {
			if (mn.name.equals(updateTickName) && mn.desc.equals(updateTickDesc)) {
				transformUpdateTick(mn);
				return classNodeToByteArray(cn);
			}
		}
		System.err.println("Failed to find the updateTick method!");
		return classNodeToByteArray(cn);
	}
}
